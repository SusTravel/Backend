package org.sustrav.demo.rest;

import com.vividsolutions.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sustrav.demo.data.PlaceRepository;
import org.sustrav.demo.data.UserRepository;
import org.sustrav.demo.data.model.Place;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.services.ImagesDomainService;
import org.sustrav.demo.services.user.UserAuthentication;
import org.sustrav.demo.utils.GeoHandler;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ImagesDomainService imagesDomainService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/api/place", method = RequestMethod.GET)
    public List<Place> getPlaces(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius
    ) {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Geometry geometry = GeoHandler.createCircle(latitude, longitude, radius);
        User user = userRepository.findById(authentication.getDetails().getId());
        if (user == null)
            return new ArrayList<>();

        List<Place> ret = placeRepository.findNearestPlaces(geometry, user.getId(), user.getPoints());
        //imagesDomainService.setDomainForEntities(ret);
        return ret;
    }

}
