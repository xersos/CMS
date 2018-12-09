package be.zwaldeck.author.controller;

import be.zwaldeck.zcms.core.plugin.PluginService;
import be.zwaldeck.zcms.core.plugin.state.ZcmsPluginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/plugins")
public class PluginController {

    private final PluginService pluginService;

    @Autowired
    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<ZcmsPluginState>> getPlugins() {
        return new ResponseEntity<>(pluginService.getPluginStates(), HttpStatus.OK);
    }

    @PostMapping("/install")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> installPlugin(@RequestParam("plugin") MultipartFile plugin) {
        pluginService.installPlugin(plugin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{pluginId}/start")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> startPlugin(@PathVariable String pluginId) {
        pluginService.startPlugin(pluginId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{pluginId}/stop")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> stopPlugin(@PathVariable String pluginId) {
        pluginService.stopPlugin(pluginId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
