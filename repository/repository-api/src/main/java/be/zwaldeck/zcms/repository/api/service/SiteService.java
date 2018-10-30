package be.zwaldeck.zcms.repository.api.service;

import be.zwaldeck.zcms.repository.api.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SiteService {

    Site create(Site site);

    Site update(Site site);

    Optional<Site> getById(String id);

    Page<Site> getSites(Pageable pageable);

    void delete(Site site);
}
