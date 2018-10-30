package be.zwaldeck.zcms.repository.rmdbs.converter;

import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.rmdbs.domain.SiteDB;
import org.springframework.stereotype.Component;

@Component
public class SiteConverterRMDBS implements DBConverter<Site, SiteDB> {

    @Override
    public SiteDB toDB(Site entity, boolean update) {
        var db = new SiteDB();

        if (update) {
            db.setId(entity.getId());
            db.setCreatedAt(entity.getCreatedAt());
        }

        db.setName(entity.getName());
        db.setPath(entity.getPath());

        return db;
    }

    @Override
    public Site fromDB(SiteDB db) {
        var entity = new Site();
        entity.setId(db.getId());
        entity.setName(db.getName());
        entity.setPath(db.getPath());
        entity.setCreatedAt(db.getCreatedAt());
        entity.setUpdatedAt(db.getUpdatedAt());

        return entity;
    }
}
