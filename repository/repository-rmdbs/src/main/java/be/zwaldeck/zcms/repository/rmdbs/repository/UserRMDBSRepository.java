package be.zwaldeck.zcms.repository.rmdbs.repository;

import be.zwaldeck.zcms.repository.rmdbs.domain.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRMDBSRepository extends JpaRepository<UserDB, String> {

    Optional<UserDB> findByUsername(String username);
}
