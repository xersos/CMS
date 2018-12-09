package be.zwaldeck.author.controller;

import be.zwaldeck.zcms.core.plugin.ZcmsPluginLoader;
import be.zwaldeck.zcms.core.plugin.state.ZcmsPluginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plugins")
public class PluginController {

    private final ZcmsPluginLoader pluginLoader;

    @Autowired
    public PluginController(ZcmsPluginLoader pluginLoader) {
        this.pluginLoader = pluginLoader;
    }

    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<ZcmsPluginState>> getPlugins() {
        return new ResponseEntity<>(pluginLoader.getPluginStates(), HttpStatus.OK);
    }

    @PostMapping("/install")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> installPlugin() {
        return null;
    }

    @PostMapping("/{pluginId}/start")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> startPlugin(@PathVariable String pluginId) {
        pluginLoader.startPlugin(pluginId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{pluginId}/stop")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> stopPlugin(@PathVariable String pluginId) {
        pluginLoader.stopPlugin(pluginId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
