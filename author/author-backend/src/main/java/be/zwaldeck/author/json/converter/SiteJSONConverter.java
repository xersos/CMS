package be.zwaldeck.author.json.converter;

import be.zwaldeck.author.json.request.SiteRequest;
import be.zwaldeck.author.json.response.SiteResponse;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.utils.UrlUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SiteJSONConverter {

    public Site fromJson(SiteRequest request) {
        var site = new Site();
        site.setName(request.getName());
        site.setPath(UrlUtils.optimizeUrl(request.getPath()));

        return site;
    }

    public SiteResponse toJson(Site site) {
        return SiteResponse.builder()
                .id(site.getId())
                .name(site.getName())
                .path(site.getPath())
                .createdAt(site.getCreatedAt())
                .updatedAt(site.getUpdatedAt())
                .build();
    }

    public Page<SiteResponse> toJson(Page<Site> sites) {
        return sites.map(this::toJson);
    }
}
