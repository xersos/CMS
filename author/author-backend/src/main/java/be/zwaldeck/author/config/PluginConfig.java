package be.zwaldeck.author.config;

import be.zwaldeck.author.ZCMSAuthorApplication;
import be.zwaldeck.zcms.core.plugin.ZcmsPluginConfig;
import be.zwaldeck.zcms.core.plugin.ZcmsPluginLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ZcmsPluginConfig.class)
@Configuration
@Slf4j
public class PluginConfig {

    private final ApplicationContext applicationContext;
    private final AbstractAutowireCapableBeanFactory beanFactory;

    @Autowired
    public PluginConfig(ApplicationContext applicationContext, AbstractAutowireCapableBeanFactory beanFactory) {
        this.applicationContext = applicationContext;

        this.beanFactory = beanFactory;
    }

    @Bean
    public ZcmsPluginLoader pluginLoader() {
        return new ZcmsPluginLoader(applicationContext, beanFactory, this::restartApplication);
    }

    private void restartApplication(String reason) {
        var restartThread = new Thread(() -> {
            log.info(reason);
            try {
               Thread.sleep(1000);
               ZCMSAuthorApplication.restart();
           } catch (InterruptedException e) {
               log.error(e.getMessage(), e);
               System.exit(9);
           }
        });
        restartThread.setDaemon(false);
        restartThread.start();
    }
}
