package be.zwaldeck.zcms.repository.rmdbs.service;

import be.zwaldeck.zcms.repository.api.exception.BrakingRepositoryException;
import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.service.PageService;
import be.zwaldeck.zcms.repository.rmdbs.converter.PageConverterRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.converter.SiteConverterRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.domain.PageDB;
import be.zwaldeck.zcms.repository.rmdbs.repository.PageRMDBSRepository;
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
        return repository.findById(id).map(converter::fromDB);
    }

    @Override
    public Page create(Page page) {
        // TODO create root Node for page
        return converter.fromDB(repository.saveAndFlush(converter.toDB(page, false)));
    }

    @Override
    public Page update(Page oldPage, Page newPage) {
        // TODO
        return null;
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
