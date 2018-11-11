package be.zwaldeck.zcms.repository.rmdbs.repository;

import be.zwaldeck.zcms.repository.rmdbs.domain.SiteDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteRMDBSRepository extends JpaRepository<SiteDB, String> {

    Optional<SiteDB> findByName(String name);

    Optional<SiteDB> findByPath(String path);
}
