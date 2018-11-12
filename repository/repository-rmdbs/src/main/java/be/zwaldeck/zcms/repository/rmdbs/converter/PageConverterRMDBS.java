package be.zwaldeck.zcms.repository.rmdbs.converter;

import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.rmdbs.domain.PageDB;
import be.zwaldeck.zcms.repository.rmdbs.domain.SiteDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PageConverterRMDBS implements DBConverter<Page, PageDB> {

    private final SiteConverterRMDBS siteConverter;

    @Autowired
    public PageConverterRMDBS(SiteConverterRMDBS siteConverter) {
        this.siteConverter = siteConverter;
    }

    @Override
    public PageDB toDB(Page page, boolean update) {
        var db = new PageDB();
        if (update) {
            db.setId(page.getId());
            db.setCreatedAt(page.getCreatedAt());
        }

        db.setName(page.getName());
        db.setTitle(page.getTitle());
        db.setDescription(page.getDescription());
        db.setPublished(page.isPublished());

        if (page.getParent() != null) {
            db.setParent(toDB(page.getParent(), true));
        }
        db.setSite(siteConverter.toDB(page.getSite(), true));
        db.setUpdatedAt(page.getUpdatedAt());

        return db;
    }

    @Override
    public Page fromDB(PageDB pageDB) {
        var page = new Page();

        page.setId(pageDB.getId());
        page.setName(pageDB.getName());
        page.setTitle(pageDB.getTitle());
        page.setDescription(pageDB.getDescription());
        page.setPublished(pageDB.isPublished());

        if (pageDB.getParent() != null) {
            page.setParent(fromDB(pageDB.getParent()));
        }

        page.setSite(siteConverter.fromDB(pageDB.getSite()));
        page.setCreatedAt(pageDB.getCreatedAt());
        page.setUpdatedAt(pageDB.getUpdatedAt());
        page.setChildren(new ArrayList<>());

        return page;
    }

    public Page fromDBWithChildren(PageDB pageDB) {
        var page = fromDB(pageDB);

        var children = new ArrayList<Page>();
        for (var child : pageDB.getChildren()) {
            children.add(fromDBWithChildren(child));
        }
        page.setChildren(children);

        return page;
    }
}
