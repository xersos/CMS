package be.zwaldeck.zcms.repository.rmdbs.service;

import be.zwaldeck.zcms.repository.api.exception.RepositoryError;
import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.service.SiteService;
import be.zwaldeck.zcms.repository.rmdbs.converter.SiteConverterRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.repository.SiteRMDBSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SiteServiceRMDBS implements SiteService {

    private final SiteRMDBSRepository repository;
    private final SiteConverterRMDBS converter;
    private final PageServiceRMDBS pageService;

    @Autowired
    public SiteServiceRMDBS(SiteRMDBSRepository repository, SiteConverterRMDBS converter, PageServiceRMDBS pageService) {
        this.repository = repository;
        this.converter = converter;
        this.pageService = pageService;
    }

    @Override
    public Site create(Site site) {
        if (repository.findByName(site.getName()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_NAME_NOT_UNIQUE);
        }

        if (repository.findByPath(site.getPath()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_PATH_NOT_UNIQUE);
        }

        site = converter.fromDB(repository.saveAndFlush(converter.toDB(site, false)));

        var page = new be.zwaldeck.zcms.repository.api.model.Page();
        page.setName("");
        page.setTitle("Home");
        page.setSite(site);

        pageService.create(page);

        return site;
    }

    @Override
    public Site update(Site oldSite, Site newSite) {

        if (!oldSite.getName().equalsIgnoreCase(newSite.getName()) && repository.findByName(newSite.getName()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_NAME_NOT_UNIQUE);
        }

        if (!oldSite.getPath().equalsIgnoreCase(newSite.getPath()) && repository.findByPath(newSite.getPath()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_PATH_NOT_UNIQUE);
        }

        newSite.setId(oldSite.getId());
        newSite.setCreatedAt(oldSite.getCreatedAt());

        return converter.fromDB(repository.saveAndFlush(converter.toDB(newSite, true)));
    }

    @Override
    public Optional<Site> getSiteById(String id) {
        return repository.findById(id).map(converter::fromDB);
    }

    @Override
    public Page<Site> getSites(Pageable pageable) {
        return repository.findAll(pageable).map(converter::fromDB);
    }

    @Override
    public void delete(Site site) {
        repository.deleteById(site.getId());
    }

}
