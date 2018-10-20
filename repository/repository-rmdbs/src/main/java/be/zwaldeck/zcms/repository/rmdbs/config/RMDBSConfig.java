package be.zwaldeck.zcms.repository.rmdbs.config;

import be.zwaldeck.zcms.utils.config.JsonConfigUtils;
import be.zwaldeck.zcms.utils.config.exception.ConfigException;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

@Configuration
@Profile("RMDBS")
@ComponentScan(basePackages = {"be.zwaldeck.zcms.repository.rmdbs"})
@EntityScan(basePackages = {"be.zwaldeck.zcms.repository.rmdbs.domain"})
@EnableJpaRepositories(basePackages = {"be.zwaldeck.zcms.repository.rmdbs.repository"})
public class RMDBSConfig {

    private DatasourceConfig datasourceConfig;

    @PostConstruct
    public void init() throws ConfigException {
        datasourceConfig = JsonConfigUtils.loadConfig("config/datasource.json", DatasourceConfig.class);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(buildConnectionUrl(datasourceConfig))
                .username(datasourceConfig.getUsername())
                .password(datasourceConfig.getPassword())
                .driverClassName(getDriver(datasourceConfig.getDatabaseType()))
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean emf() {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan(getEmfPackagesToScan(datasourceConfig.getPackagesToScan()));
        emf.setPersistenceUnitName("zcms");

        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(false);

        var jpaProps = new Properties();
        jpaProps.put("hibernate.dialect", getHibernateDialect(datasourceConfig.getDatabaseType()));

        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(jpaProps);
        emf.afterPropertiesSet();

        return emf;
    }

    @Bean(name = "rawSettingsMap")
    public Map<String, String> rawSettingsMap() {
        var settings = new HashMap<String, String>();

        settings.put("connection.driver_class", getDriver(datasourceConfig.getDatabaseType()));
        settings.put("dialect", getHibernateDialect(datasourceConfig.getDatabaseType()));
        settings.put("hibernate.connection.url", buildConnectionUrl(datasourceConfig));
        settings.put("hibernate.connection.username", datasourceConfig.getUsername());
        settings.put("hibernate.connection.password", datasourceConfig.getPassword());
        settings.put("show_sql", "true");

        return settings;
    }

    private String buildConnectionUrl(DatasourceConfig config) {
        var connectionString = "jdbc:";
        connectionString += getConnectionUrlType(config.getDatabaseType()) + "://";
        connectionString += config.getHost() + ":" + config.getPort();
        connectionString += "/" + config.getDatabaseName();
        connectionString += "?useSSL=" + config.isUseSSL();

        return connectionString;
    }

    private String getConnectionUrlType(DatabaseType type) {
        switch (type) {
            case POSTGRES:
                return "postgres";
            case MYSQL:
            default:
                return "mysql";
        }
    }

    private String getDriver(DatabaseType type) {
        switch (type) {
            case POSTGRES:
                return "org.postgres.Driver";
            case MYSQL:
            default:
                return "com.mysql.jdbc.Driver";
        }
    }

    private String[] getEmfPackagesToScan(String[] packages) {
        var toScan = new ArrayList<String>();
        toScan.add("be.zwaldeck.zcms.repository.rmdbs.domain");

        toScan.addAll(Arrays.asList(packages));

        return toScan.toArray(new String[0]);
    }

    private String getHibernateDialect(DatabaseType type) {
        switch (type) {
            case POSTGRES:
                return "org.hibernate.dialect.PostgreSQL95Dialect";
            case MYSQL:
            default:
                return "org.hibernate.dialect.MySQLDialect";
        }
    }
}
