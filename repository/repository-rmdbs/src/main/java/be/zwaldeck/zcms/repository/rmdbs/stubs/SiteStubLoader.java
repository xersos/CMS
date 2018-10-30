package be.zwaldeck.zcms.repository.rmdbs.stubs;

import be.zwaldeck.zcms.repository.api.model.Role;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.rmdbs.service.SiteServiceRMDBS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SiteStubLoader {

    private SiteStubLoader() {
    }

    public static List<Site> load(SiteServiceRMDBS siteService) {
        var sites = new ArrayList<Site>();

        var kinext = Site.builder()
                .name("kinext")
                .path("/kinext")
                .build();

        var feelio = Site.builder()
                .name("feel-io")
                .path("/feel-io")
                .build();

        sites.add(siteService.create(kinext));
        sites.add(siteService.create(feelio));

        return sites;
    }
}
