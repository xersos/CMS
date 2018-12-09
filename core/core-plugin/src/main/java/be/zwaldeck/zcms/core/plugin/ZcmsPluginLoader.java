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
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class ZcmsPluginLoader {

    private static final String PLUGIN_DIR = "plugins";
    private static final String PLUGIN_STATE_JSON_NAME = "plugin-state.json";

    private final ApplicationContext applicationContext;
    private final AbstractAutowireCapableBeanFactory beanFactory;
    private final Consumer<String> restartConsumer;
    private final ZcmsPluginManager pluginManager;
    private final Path stateJsonPath;
    private final ObjectMapper objectMapper;

    @Getter
    private List<ZcmsPluginState> pluginStates = new ArrayList<>();

    public ZcmsPluginLoader(ApplicationContext applicationContext, AbstractAutowireCapableBeanFactory beanFactory,
                            Consumer<String> restartConsumer) {
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
        this.restartConsumer = restartConsumer;

        var pluginPath = Paths.get(System.getProperty("user.dir"), PLUGIN_DIR);
        log.info("Plugin path: '{}'", pluginPath);
        pluginManager = new ZcmsPluginManager(pluginPath);
        pluginManager.setApplicationContext(applicationContext);
        pluginManager.loadPlugins();

        stateJsonPath = Paths.get(System.getProperty("user.dir"), PLUGIN_DIR, PLUGIN_STATE_JSON_NAME);
        objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void init() throws IOException {
        if (Files.exists(stateJsonPath)) {
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

    private void loadFromJson() throws IOException {
        pluginStates = objectMapper.readValue(stateJsonPath.toFile(),
                new TypeReference<List<ZcmsPluginState>>(){});

        if (pluginStates != null && pluginStates.size() != pluginManager.getPlugins().size()) {
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
            objectMapper.writeValue(stateJsonPath.toFile(), pluginStates);
        } catch (IOException e) {
            log.error("There was an issue while write down the plugin states to the json file.", e);
        }
    }

}
