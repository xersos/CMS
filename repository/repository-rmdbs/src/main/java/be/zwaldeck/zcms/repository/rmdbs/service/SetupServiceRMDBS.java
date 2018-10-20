package be.zwaldeck.zcms.repository.rmdbs.service;

import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.model.Role;
import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.api.service.SetupService;
import be.zwaldeck.zcms.repository.rmdbs.domain.UserDB;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

@Service
public class SetupServiceRMDBS implements SetupService {

    private static final String[] TABLES_NEEDED = {
      "user_tbl",
      "user_roles_tbl"
    };

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceRMDBS userService;
    private final Map<String, String> rawSettings;

    @Autowired
    public SetupServiceRMDBS(DataSource dataSource, PasswordEncoder passwordEncoder, UserServiceRMDBS userService,
                             @Qualifier("rawSettingsMap") Map<String, String> rawSettings) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.rawSettings = rawSettings;
    }

    @Override
    public boolean isRepositorySetup() throws RepositoryException {
        try {
            var tablesInDb = new ArrayList<String>();
            var resultSet = dataSource.getConnection().getMetaData().getTables(null, null, "%", null);
            while (resultSet.next()) {
                tablesInDb.add(resultSet.getString("TABLE_NAME"));
            }

            for (var table : TABLES_NEEDED) {
                if (!tablesInDb.contains(table)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("There went something wrong", e);
        }

        return true;
    }

    @Override
    public void setupRepository() throws RepositoryException {
        var metaDataSrc = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(rawSettings).build()
        );
        metaDataSrc.addAnnotatedClass(UserDB.class);

        var schemaExport = new SchemaExport();
        schemaExport.setHaltOnError(false);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.execute(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), SchemaExport.Action.BOTH,
                metaDataSrc.buildMetadata());

        var admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Arrays.asList(Role.values()));
        userService.create(admin);

        // TODO create site obj
    }
}
