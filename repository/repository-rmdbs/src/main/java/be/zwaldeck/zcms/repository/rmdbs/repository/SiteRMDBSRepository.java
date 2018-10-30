package be.zwaldeck.zcms.repository.rmdbs.repository;

import be.zwaldeck.zcms.repository.rmdbs.domain.SiteDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRMDBSRepository extends JpaRepository<SiteDB, String> {

}
