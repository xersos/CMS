package be.zwaldeck.author.json.converter;

import be.zwaldeck.author.exception.AuthorError;
import be.zwaldeck.author.exception.AuthorException;
import be.zwaldeck.author.json.request.PageRequest;
import be.zwaldeck.author.json.response.PageResponse;
import be.zwaldeck.zcms.repository.api.model.Page;
import be.zwaldeck.zcms.repository.api.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageJSONConverter {

    private final SiteJSONConverter siteConverter;
    private final PageService pageService;

    @Autowired
    public PageJSONConverter(SiteJSONConverter siteConverter, PageService pageService) {
        this.siteConverter = siteConverter;
        this.pageService = pageService;
    }

    public Page fromJson(PageRequest request) {

        var parent = pageService.getPageById(request.getParent());

        var page = new Page();
        page.setName(request.getName());
        page.setTitle(request.getTitle());
        page.setParent(parent.orElseThrow(() -> new AuthorException(AuthorError.PAGE_PARENT_NOT_FOUND)));

        return page;
    }

    public PageResponse toJson(Page page) {
        var res = toJsonForDetail(page);
        res.setParent(toJsonForDetail(page.getParent()));

        return res;
    }

    public org.springframework.data.domain.Page<PageResponse> toJson(org.springframework.data.domain.Page<Page> pages) {
        return pages.map(this::toJsonForList);
    }

    private PageResponse toJsonForDetail(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .name(page.getName())
                .title(page.getTitle())
                .path(page.getPath())
                .published(page.isPublished())
                .site(siteConverter.toJson(page.getSite()))
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .build();
    }

    private PageResponse toJsonForList(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .name(page.getName())
                .title(page.getTitle())
                .path(page.getPath())
                .published(page.isPublished())
                .site(null)
                .parent(null)
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .build();
    }
}
