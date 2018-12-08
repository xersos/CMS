package be.zwaldeck.author;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
public class ZCMSAuthorApplication {

    public static final String[] PUBLIC_MEDIA_ROUTES = {

    };

    private static String[] args;
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        ZCMSAuthorApplication.args = args;
        applicationContext = SpringApplication.run(ZCMSAuthorApplication.class, args);
    }

    public static void restart() {
        applicationContext.close();
        applicationContext = SpringApplication.run(ZCMSAuthorApplication.class, args);
    }

    // todo move to seperate config
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setThreadNamePrefix("zcms-");
        executor.initialize();

        return executor;
    }
}
