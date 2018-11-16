package be.zwaldeck.author.controller;

import be.zwaldeck.author.json.converter.SiteJSONConverter;
import be.zwaldeck.author.json.request.SiteRequest;
import be.zwaldeck.author.json.response.SiteResponse;
import be.zwaldeck.zcms.core.common.rest.error.NotFoundException;
import be.zwaldeck.zcms.core.common.rest.pagination.PaginatedResponse;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sites")
public class SiteController {

    private final SiteJSONConverter jsonConverter;
    private final SiteService siteService;

    @Autowired
    public SiteController(SiteJSONConverter jsonConverter, SiteService siteService) {
        this.jsonConverter = jsonConverter;
        this.siteService = siteService;
    }

    @Secured("ROLE_AUTHOR")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<SiteResponse> createSite(@RequestBody @Valid SiteRequest request) {
        var site = siteService.create(jsonConverter.fromJson(request));
        return new ResponseEntity<>(jsonConverter.toJson(site), HttpStatus.CREATED);
    }

    @Secured("ROLE_AUTHOR")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<SiteResponse>> getSites(Pageable pageable) {
        var sites = jsonConverter.toJson(siteService.getSites(pageable));
        return new ResponseEntity<>(PaginatedResponse.createResponse(sites, "/sites"), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}",
            method = RequestMethod.GET)
    public ResponseEntity<SiteResponse> getSite(@PathVariable String id) {
        return new ResponseEntity<>(jsonConverter.toJson(getSiteById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}",
            method = RequestMethod.PUT)
    public ResponseEntity<SiteResponse> updateSite(@PathVariable String id,
                                                   @RequestBody @Valid SiteRequest request) {
        var oldSite = getSiteById(id);
        var newSite = jsonConverter.fromJson(request);
        var site = siteService.update(oldSite, newSite);
        return new ResponseEntity<>(jsonConverter.toJson(site), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delteSite(@PathVariable String id) {
        siteService.getSiteById(id).ifPresent(siteService::delete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Site getSiteById(String id) {
        return siteService.getSiteById(id)
                .orElseThrow(() -> new NotFoundException("Could not find a site with the id: '" + id + "'"));
    }

}
