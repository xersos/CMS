package be.zwaldeck.zcms.core.plugin;

import be.zwaldeck.zcms.core.plugin.exception.PluginError;
import be.zwaldeck.zcms.core.plugin.exception.ZcmsPluginException;
import be.zwaldeck.zcms.core.plugin.state.ZcmsPluginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PluginService {

    private final ZcmsPluginLoader pluginLoader;

    @Autowired
    public PluginService(ZcmsPluginLoader pluginLoader) {
        this.pluginLoader = pluginLoader;
    }

    public List<ZcmsPluginState> getPluginStates() {
        return pluginLoader.getPluginStates();
    }

    public void startPlugin(String pluginId) {
        pluginLoader.startPlugin(pluginId);
    }

    public void stopPlugin(String pluginId) {
        pluginLoader.stopPlugin(pluginId);
    }

    public void installPlugin(MultipartFile file) {
        // TODO: what in case of a version update?
        try {
            file.transferTo(Paths.get(ZcmsPluginConfig.PLUGIN_PATH.toString(), file.getOriginalFilename()));
            pluginLoader.loadPlugin(file.getOriginalFilename());
        } catch (IOException e) {
            throw new ZcmsPluginException(PluginError.INSTALL_ERROR, e);
        }
    }

    public void deletePlugin(String pluginId) {
        try {
        var pluginPath = pluginLoader.unloadPlugin(pluginId);
        Files.deleteIfExists(pluginPath);
        } catch (IOException e) {
            throw new ZcmsPluginException(PluginError.DELETE_ERROR, e);
        }
    }
}
