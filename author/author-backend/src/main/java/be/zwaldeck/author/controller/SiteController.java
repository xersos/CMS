package be.zwaldeck.author.controller;

import be.zwaldeck.author.json.converter.SiteJSONConverter;
import be.zwaldeck.author.json.response.SiteResponse;
import be.zwaldeck.zcms.core.common.rest.pagination.PaginatedResponse;
import be.zwaldeck.zcms.repository.api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<SiteResponse>> getSites(Pageable pageable) {
        var sites = jsonConverter.toJson(siteService.getSites(pageable));
        return new ResponseEntity<>(PaginatedResponse.createResponse(sites, "/sites"), HttpStatus.OK);
    }


}
