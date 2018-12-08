package be.zwaldeck.zcms.core.plugin;

import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.spring.SpringPluginManager;

import java.nio.file.Path;

public class ZcmsPluginManager extends SpringPluginManager {

    public ZcmsPluginManager(Path pluginsRoot) {
        super(pluginsRoot);
    }

    @Override
    protected PluginDescriptorFinder createPluginDescriptorFinder() {
        return new ManifestPluginDescriptorFinder();
    }

    public void init() {
        throw new UnsupportedOperationException("Do not use this method");
    }
}
