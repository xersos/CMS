package be.zwaldeck.author.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("RMDBS")
@Import(be.zwaldeck.zcms.repository.rmdbs.config.RMDBSConfig.class)
@Configuration
public class RMDBSConfig {
}
