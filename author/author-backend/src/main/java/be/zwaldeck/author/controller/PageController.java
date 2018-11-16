package be.zwaldeck.author.controller;

import be.zwaldeck.author.json.converter.PageJSONConverter;
import be.zwaldeck.author.json.response.PageResponse;
import be.zwaldeck.author.json.response.TreeItemResponse;
import be.zwaldeck.author.service.TreeViewService;
import be.zwaldeck.zcms.core.common.rest.error.NotFoundException;
import be.zwaldeck.zcms.core.common.rest.pagination.PaginatedResponse;
import be.zwaldeck.zcms.repository.api.exception.BrakingRepositoryException;
import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.service.PageService;
import be.zwaldeck.zcms.repository.api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sites/{siteId:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}")
public class PageController {

    private final PageService pageService;
    private final PageJSONConverter pageConverter;
    private final SiteService siteService;
    private final TreeViewService treeViewService;

    @Autowired
    public PageController(PageService pageService, PageJSONConverter pageConverter,
                          SiteService siteService, TreeViewService treeViewService) {
        this.pageService = pageService;
        this.pageConverter = pageConverter;
        this.siteService = siteService;
        this.treeViewService = treeViewService;
    }

    @RequestMapping(value = "/page-tree", method = RequestMethod.GET)
    public ResponseEntity<TreeItemResponse> getSitePageTree(@PathVariable String siteId) throws BrakingRepositoryException {
        Site site = getSiteById(siteId);

        return new ResponseEntity<>(treeViewService.getTreeViewBySite(site), HttpStatus.OK);
    }


    @RequestMapping(value = "/pages", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<PageResponse>> getPages(
            Pageable pageable, @PathVariable String siteId,
            @RequestParam(value = "parent", required = true, defaultValue = "") String parentId,
            @RequestParam(value = "includeParent", required = false, defaultValue = "false") boolean includeParent
    ) {

        var site = getSiteById(siteId);
        var parent = pageService.getPageById(parentId)
                .orElseThrow(() -> new NotFoundException("Could not find a page with id '" + parentId + "'"));
        var pages = pageService.getPagesBySiteAndParent(site, parent, includeParent, pageable);

        var baseUrl = "/sites/" + siteId + "/pages";
        return new ResponseEntity<>(PaginatedResponse.createResponse(pageConverter.toJson(pages), baseUrl), HttpStatus.OK);
    }
    private Site getSiteById(String id) {
        return siteService.getSiteById(id).
                orElseThrow(() -> new NotFoundException("Could not find a site with the id: '" + id  +"'"));
    }
}
