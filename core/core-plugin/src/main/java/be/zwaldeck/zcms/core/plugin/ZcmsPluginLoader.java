package be.zwaldeck.zcms.core.plugin;

import be.zwaldeck.zcms.core.plugin.exception.PluginError;
import be.zwaldeck.zcms.core.plugin.exception.ZcmsPluginException;
import be.zwaldeck.zcms.core.plugin.state.ZcmsPluginState;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static be.zwaldeck.zcms.core.plugin.ZcmsPluginConfig.PLUGIN_PATH;
import static be.zwaldeck.zcms.core.plugin.ZcmsPluginConfig.STATE_JSON_PATH;


@Service
@Slf4j
public class ZcmsPluginLoader {

    private final ApplicationContext applicationContext;
    private final AbstractAutowireCapableBeanFactory beanFactory;
    private final Consumer<String> restartConsumer;
    private final ZcmsPluginManager pluginManager;
    private final ObjectMapper objectMapper;

    @Getter
    private List<ZcmsPluginState> pluginStates = new ArrayList<>();

    @Autowired
    public ZcmsPluginLoader(ApplicationContext applicationContext, AbstractAutowireCapableBeanFactory beanFactory,
                            @Qualifier("restartConsumer") Consumer<String> restartConsumer) {
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
        this.restartConsumer = restartConsumer;


        log.info("Plugin path: '{}'", PLUGIN_PATH);
        pluginManager = new ZcmsPluginManager(PLUGIN_PATH);
        pluginManager.setApplicationContext(applicationContext);
        pluginManager.loadPlugins();


        objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void init() throws IOException {
        if (Files.exists(STATE_JSON_PATH)) {
            loadFromJson();
        } else {
            loadFromScratch();
        }
    }

    public void startPlugin(String pluginId) {
        var pluginWrapper = pluginManager.getPlugin(pluginId);
        if (pluginWrapper == null) {
            throw new ZcmsPluginException(PluginError.PLUGIN_NOT_FOUND);
        }

        if (pluginWrapper.getPluginState().equals(PluginState.STARTED)) {
            log.info("Plugin '{}' already started.", pluginId);
            return;
        }

        pluginManager.startPlugin(pluginId);
        pluginStates.stream()
                .filter(state -> state.getId().equals(pluginId))
                .findFirst()
                .ifPresent(state -> state.setState("STARTED"));
        rewriteStateFile();
        restartConsumer.accept("Restart application because we started plugin '" + pluginId + "'");
    }

    public void stopPlugin(String pluginId) {

        var pluginWrapper = pluginManager.getPlugin(pluginId);
        if (pluginWrapper == null) {
            throw new ZcmsPluginException(PluginError.PLUGIN_NOT_FOUND);
        }

        if (pluginWrapper.getPluginState().equals(PluginState.STOPPED)) {
            log.info("Plugin '{}' already stopped.", pluginId);
            return;
        }

        pluginManager.stopPlugin(pluginId);
        pluginStates.stream()
                .filter(state -> state.getId().equals(pluginId))
                .findFirst()
                .ifPresent(state -> state.setState("STOPPED"));
        rewriteStateFile();
        restartConsumer.accept("Restart application because we stopped plugin '" + pluginId + "'");
    }

    public void loadPlugin(String jarName)  {
        var pluginId = pluginManager.loadPlugin(PLUGIN_PATH.resolve(jarName));
        pluginStates.add(new ZcmsPluginState(pluginId, "STOPPED"));
        startPlugin(pluginId);
        rewriteStateFile();
    }

    /**
     * @param pluginId the id of the plugin you want to unload
     * @return the path of the unloaded plugin
     */
    public Path unloadPlugin(String pluginId) {
        var pluginWrapper = pluginManager.getPlugin(pluginId);

        if (pluginWrapper == null) {
            throw new ZcmsPluginException(PluginError.PLUGIN_NOT_FOUND);
        }

        var path = pluginWrapper.getPluginPath();

        pluginStates.stream()
                .filter(state -> state.getId().equals(pluginId))
                .findFirst()
                .ifPresent(state -> {
                    pluginManager.unloadPlugin(pluginId);
                    pluginStates.remove(state);
                });

        rewriteStateFile();

        return path;
    }

    private void loadFromJson() throws IOException {
        pluginStates = objectMapper.readValue(STATE_JSON_PATH.toFile(),
                new TypeReference<List<ZcmsPluginState>>(){});

        if (pluginStates == null || pluginStates.size() != pluginManager.getPlugins().size()) {
            loadFromScratch();
            return;
        }

        pluginStates.stream()
                .filter(state -> Arrays.asList(ZcmsPluginState.ALLOWED_PLUGIN_STATES).contains(state.getState().toUpperCase()))
                .filter(state -> state.getState().equalsIgnoreCase("STARTED"))
                .peek(state -> System.out.println(state.getState()))
                .peek(state -> pluginManager.startPlugin(state.getId()))
                .map(state -> pluginManager.getPlugin(state.getId()))
                .forEach(this::registerBeansForPlugin);

    }

    private void loadFromScratch() {
        pluginManager.startPlugins();
        pluginManager.getStartedPlugins().stream()
                .peek(pluginWrapper -> pluginStates.add(new ZcmsPluginState(pluginWrapper.getPluginId(), "STARTED")))
                .forEach(this::registerBeansForPlugin);

        rewriteStateFile();
    }

    private void registerBeansForPlugin(PluginWrapper pluginWrapper) {
        var pluginId = pluginWrapper.getPluginId();
        log.debug("Registering extensions of the plugin '{}' as beans.", pluginId);
        pluginManager.getExtensionClassNames(pluginId)
                .forEach((className) -> registerBeanForExtension(pluginWrapper, className));

    }

    private void registerBeanForExtension(PluginWrapper pluginWrapper, String className) {
        try {
            log.debug("Register extensions '{}' as bean", className);
            var extension = pluginWrapper.getPluginClassLoader().loadClass(className);
            beanFactory.registerSingleton(className, pluginManager.getExtensionFactory().create(extension));
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void rewriteStateFile() {
        try {
            objectMapper.writeValue(STATE_JSON_PATH.toFile(), pluginStates);
        } catch (IOException e) {
            log.error("There was an issue while writing down the plugin states to the json file.", e);
        }
    }

}
