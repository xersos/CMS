package be.zwaldeck.zcms.repository.api.service;

import be.zwaldeck.zcms.repository.api.exception.BrakingRepositoryException;
import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.model.Site;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PageService {

    Optional<Page> getPageById(String id);

    Page create(Page page);

    Page update(Page oldPage, Page newPage);

    void delete(Page page);

    Optional<Page> getPageByName(String name);

    List<Page> getPagesBySite(Site site);

    org.springframework.data.domain.Page<Page> getPagesBySiteAndParent(Site site, Page parent,
                                                                       boolean includeParent, Pageable pageable);

    Page getRootPageForSite(Site site) throws BrakingRepositoryException;

    org.springframework.data.domain.Page<Page> getPagesBySite(Site site, Pageable pageable);
}
