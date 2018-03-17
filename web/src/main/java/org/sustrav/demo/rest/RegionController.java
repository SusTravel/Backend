package org.sustrav.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.sustrav.demo.data.RegionRepository;
import org.sustrav.demo.data.model.Region;
import org.sustrav.demo.services.ImagesDomainService;

import java.util.List;

@RestController
public class RegionController {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ImagesDomainService imagesDomainService;

    @RequestMapping(value = "/api/continent/{code}/region", method = RequestMethod.GET)
    public List<Region> getContinents(@PathVariable("code") String code) {
        List<Region> ret = regionRepository.findAllByContinentCode(code);
        imagesDomainService.setDomainForEntities(ret);
        return ret;
    }
}
