package be.zwaldeck.zcms.repository.rmdbs.service;

import be.zwaldeck.zcms.repository.api.exception.BrakingRepositoryException;
import be.zwaldeck.zcms.repository.api.exception.RepositoryError;
import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.service.PageService;
import be.zwaldeck.zcms.repository.rmdbs.converter.PageConverterRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.converter.SiteConverterRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.domain.PageDB;
import be.zwaldeck.zcms.repository.rmdbs.repository.PageRMDBSRepository;
import be.zwaldeck.zcms.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PageServiceRMDBS implements PageService {

    private final PageRMDBSRepository repository;
    private final PageConverterRMDBS converter;
    private final SiteConverterRMDBS siteConverter;

    @Autowired
    public PageServiceRMDBS(PageRMDBSRepository repository, PageConverterRMDBS pageConverter, SiteConverterRMDBS siteConverter) {
        this.repository = repository;
        this.converter = pageConverter;
        this.siteConverter = siteConverter;
    }

    @Override
    public Optional<Page> getPageById(String id) {
        return repository.findById(id).map(converter::fromDBWithChildren);
    }

    @Override
    public Page create(Page page) {
        // TODO create root Node for page

        // TODO REFACTOR to something simpler
        if (page.getParent() != null &&
                page.getParent().getChildren().stream().anyMatch(child -> child.getName().equals(page.getName()))) {
            throw new RepositoryException(RepositoryError.PAGE_NAME_NOT_UNIQUE);
        }

        var basePath = page.getParent() != null ? UrlUtils.addTrailingSlash(page.getParent().getPath()) : "/";
        page.setPath(UrlUtils.optimizeUrl(basePath + page.getName()));

        return converter.fromDB(repository.saveAndFlush(converter.toDB(page, false)));
    }

    @Override
    public Page update(Page oldPage, Page newPage) {
        newPage.setSite(oldPage.getSite());
        newPage.setId(oldPage.getId());
        newPage.setChildren(oldPage.getChildren());
        newPage.setPublished(oldPage.isPublished());
        newPage.setCreatedAt(oldPage.getCreatedAt());

        // TODO REFACTOR to something simpler
        if (newPage.getParent() != null && !newPage.getName().equals(oldPage.getName()) &&
                newPage.getParent().getChildren().stream().anyMatch(child -> child.getName().equals(newPage.getName()))) {
            throw new RepositoryException(RepositoryError.PAGE_NAME_NOT_UNIQUE);
        }

        var basePath = newPage.getParent() != null ? UrlUtils.addTrailingSlash(newPage.getParent().getPath()) : "/";
        newPage.setPath(UrlUtils.optimizeUrl(basePath + newPage.getName()));

        return converter.fromDB(repository.saveAndFlush(converter.toDB(newPage, true)));
    }

    @Override
    public void delete(Page page) {
        repository.delete(converter.toDB(page, true));
    }

    @Override
    public Optional<Page> getPageByName(String name) {
        return repository.findByName(name).map(converter::fromDB);
    }

    @Override
    public List<Page> getPagesBySite(Site site) {
        return repository.findBySite(siteConverter.toDB(site, true))
                .stream()
                .map(converter::fromDB)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<Page> getPagesBySite(Site site, Pageable pageable) {
        return repository.findBySite(siteConverter.toDB(site, true), pageable)
                .map(converter::fromDB);
    }

    @Override
    public org.springframework.data.domain.Page<Page> getPagesBySiteAndParent(Site site, Page parent, boolean includeParent, Pageable pageable) {
        var siteDB = siteConverter.toDB(site, true);
        var parentDB = converter.toDB(parent, true);
        org.springframework.data.domain.Page<PageDB> pages;

        if (includeParent) {
            pages = repository.findBySiteAndParentOrderByNameAscIncludeParent(siteDB, parentDB, parentDB.getId(), pageable);
        } else {
            pages = repository.findBySiteAndParentOrderByNameAsc(siteDB, parentDB, pageable);
        }

        return pages.map(converter::fromDB);
    }

    @Override
    public Page getRootPageForSite(Site site) throws BrakingRepositoryException {
        return repository.findBySiteAndParentIsNull(siteConverter.toDB(site, true))
                .map(converter::fromDBWithChildren)
                .orElseThrow(() -> new BrakingRepositoryException("This site does not have a root. This should not be possible"));
    }
}
