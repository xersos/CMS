package be.zwaldeck.zcms.repository.rmdbs.service;

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

    @Autowired
    public SiteServiceRMDBS(SiteRMDBSRepository repository, SiteConverterRMDBS converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public Site create(Site site) {
        return converter.fromDB(repository.saveAndFlush(converter.toDB(site, false)));
    }

    @Override
    public Site update(Site site) {
        return converter.fromDB(repository.saveAndFlush(converter.toDB(site, true)));
    }

    @Override
    public Optional<Site> getById(String id) {
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
