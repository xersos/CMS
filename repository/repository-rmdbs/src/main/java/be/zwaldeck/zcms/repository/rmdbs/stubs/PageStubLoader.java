package be.zwaldeck.zcms.repository.rmdbs.stubs;

import be.zwaldeck.zcms.repository.api.exception.BrakingRepositoryException;
import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.rmdbs.service.PageServiceRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.service.SiteServiceRMDBS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PageStubLoader {

    private PageStubLoader() {
    }

    public static List<Page> load(PageServiceRMDBS pageService, List<Site> sites) {
        var pages = new ArrayList<Page>();

        try {
            for (var site : sites) {
                var rootPage = pageService.getRootPageForSite(site);

                var en = new Page();
                en.setName("en");
                en.setParent(rootPage);
                en.setTitle(site.getName() + " | En");
                en.setDescription("");
                en.setPublished(false);
                en.setSite(site);
                en = pageService.create(en);

                var page1 = new Page();
                page1.setName("page1");
                page1.setParent(en);
                page1.setTitle(site.getName() + " | Page1");
                page1.setDescription("");
                page1.setPublished(false);
                page1.setSite(site);
                page1 = pageService.create(page1);

                var sales = new Page();
                sales.setName("sales");
                sales.setParent(en);
                sales.setTitle(site.getName() + " | sales");
                sales.setDescription("");
                sales.setPublished(false);
                sales.setSite(site);
                sales = pageService.create(sales);

                var smartPhones = new Page();
                smartPhones.setName("smartphones");
                smartPhones.setParent(sales);
                smartPhones.setTitle(site.getName() + " | smart phones");
                smartPhones.setDescription("");
                smartPhones.setPublished(false);
                smartPhones.setSite(site);
                smartPhones = pageService.create(smartPhones);

                var leMax2 = new Page();
                leMax2.setName("le-max2");
                leMax2.setParent(smartPhones);
                leMax2.setTitle(site.getName() + " | Le Max 2");
                leMax2.setDescription("");
                leMax2.setPublished(false);
                leMax2.setSite(site);
                leMax2 = pageService.create(leMax2);

                var page3 = new Page();
                page3.setName("page3");
                page3.setParent(en);
                page3.setTitle(site.getName() + " | page3");
                page3.setDescription("");
                page3.setPublished(false);
                page3.setSite(site);
                page3 = pageService.create(page3);

                var page3_1 = new Page();
                page3_1.setName("page3-1");
                page3_1.setParent(page3);
                page3_1.setTitle(site.getName() + " | page3-1");
                page3_1.setDescription("");
                page3_1.setPublished(false);
                page3_1.setSite(site);
                page3_1 = pageService.create(page3_1);

                var page3_1_1 = new Page();
                page3_1_1.setName("page3-1-1");
                page3_1_1.setParent(page3_1);
                page3_1_1.setTitle(site.getName() + " | page3-1-1");
                page3_1_1.setDescription("");
                page3_1_1.setPublished(false);
                page3_1_1.setSite(site);
                page3_1_1 = pageService.create(page3_1_1);

                var page3_2 = new Page();
                page3_2.setName("page3-2");
                page3_2.setParent(page3);
                page3_2.setTitle(site.getName() + " | page3-2");
                page3_2.setDescription("");
                page3_2.setPublished(false);
                page3_2.setSite(site);
                page3_2 = pageService.create(page3_2);

                pages.addAll(Arrays.asList(en, page1, sales, smartPhones, leMax2, page3, page3_1, page3_1_1, page3_2));
            }
        } catch (BrakingRepositoryException e) {
            e.printStackTrace();
            System.exit(9);
        }

        return pages;
    }
}
