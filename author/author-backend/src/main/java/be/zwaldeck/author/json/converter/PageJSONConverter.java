package be.zwaldeck.author.json.converter;

import be.zwaldeck.author.json.response.PageResponse;
import be.zwaldeck.author.json.response.SiteResponse;
import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.model.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageJSONConverter {

    private final SiteJSONConverter siteConverter;

    @Autowired
    public PageJSONConverter(SiteJSONConverter siteConverter) {
        this.siteConverter = siteConverter;
    }

//    public Site fromJson(SiteRequest request) {
//        var site = new Site();
//        site.setName(request.getName());
//        site.setPath(UrlUtils.optimizeUrl(request.getPath()));
//
//        return site;
//    }

    public PageResponse toJson(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .name(page.getName())
                .title(page.getTitle())
                .description(page.getDescription())
                .published(page.isPublished())
                .site(siteConverter.toJson(page.getSite()))
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .build();
    }

    public org.springframework.data.domain.Page<PageResponse> toJson(org.springframework.data.domain.Page<Page> pages) {
        return pages.map(this::toJson);
    }
}
