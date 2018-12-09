package be.zwaldeck.zcms.core.plugin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ComponentScan(basePackages = {"be.zwaldeck.zcms.core.plugin"})
public class ZcmsPluginConfig {

    private static final String PLUGIN_DIR = "plugins";
    private static final String PLUGIN_STATE_JSON_NAME = "plugin-state.json";

    public static final Path PLUGIN_PATH = Paths.get(System.getProperty("user.dir"), PLUGIN_DIR);
    public static final Path STATE_JSON_PATH = Paths.get(System.getProperty("user.dir"), PLUGIN_DIR, PLUGIN_STATE_JSON_NAME);
}
