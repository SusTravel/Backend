package org.sustrav.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.sustrav.demo.data.ContinentRepository;
import org.sustrav.demo.data.model.Continent;
import org.sustrav.demo.services.ImagesDomainService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ContinentController {

    @Autowired
    private ContinentRepository continentRepository;

    @Autowired
    private ImagesDomainService imagesDomainService;

    @RequestMapping(value = "/api/continent", method = RequestMethod.GET)
    public List<Continent> getContinents(HttpServletRequest request) {
        List<Continent> ret = continentRepository.findAll();
        //imagesDomainService.setDomainForEntities(ret);
        return ret;
    }
}
