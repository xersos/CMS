package be.zwaldeck.zcms.repository.rmdbs.repository;

import be.zwaldeck.zcms.repository.rmdbs.domain.PageDB;
import be.zwaldeck.zcms.repository.rmdbs.domain.SiteDB;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PageRMDBSRepository extends JpaRepository<PageDB, String> {

    Optional<PageDB> findByName(String name);

    List<PageDB> findBySite(SiteDB site);

    Page<PageDB> findBySite(SiteDB site, Pageable pageable);

    Page<PageDB> findBySiteAndParentOrderByNameAsc(SiteDB site, PageDB parent, Pageable pageable);

    @Query("SELECT p FROM PageDB p WHERE (p.site = ?1 AND p.parent = ?2) OR p.id = ?3")
    Page<PageDB> findBySiteAndParentOrderByNameAscIncludeParent(SiteDB site, PageDB parent,
                                                                String id, Pageable pageable);

    Optional<PageDB> findBySiteAndParentIsNull(SiteDB site);
}
