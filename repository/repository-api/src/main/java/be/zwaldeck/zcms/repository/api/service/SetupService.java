package be.zwaldeck.zcms.repository.api.service;

import be.zwaldeck.zcms.repository.api.exception.RepositoryException;

import java.sql.SQLException;

public interface SetupService {

    boolean isRepositorySetup() throws SQLException;

    void setupRepository();

}
