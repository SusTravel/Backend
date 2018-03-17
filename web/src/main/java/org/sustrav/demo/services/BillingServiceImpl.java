package org.sustrav.demo.services;

import com.vividsolutions.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.sustrav.demo.data.PlaceRepository;
import org.sustrav.demo.data.UserRepository;
import org.sustrav.demo.data.VisitedPlaceRepository;
import org.sustrav.demo.data.model.Place;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.data.model.VisitedPlace;
import org.sustrav.demo.services.user.UserAuthentication;
import org.sustrav.demo.utils.GeoHandler;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private PlaceRepository placeRepository;

    @Value("${billing.activationRadius}")
    private double activationRadius;

    @Autowired
    private VisitedPlaceRepository visitedPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public int acceptPoint(long pointId, double latitude, double longitude) {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(authentication.getDetails().getId());
        if (user == null)
            throw new UserNotAvailableException();

        Geometry circle = GeoHandler.createCircle(latitude, longitude, activationRadius);
        Place place = placeRepository.findPlaceInsideLocation(circle, pointId);
        if (place == null)
            throw new PointNotAvailableException();


        if (visitedPlaceRepository.findByUserAndPlace(user, place).size() > 0)
            return user.getPoints();

        VisitedPlace visit = new VisitedPlace();
        visit.setUser(user);
        visit.setPlace(place);
        visit.setPoints(place.getCostInPoints());

        visitedPlaceRepository.save(visit);

        user.getVisitedPlaces().add(visit);
        user.setPoints(user.getPoints() + place.getCostInPoints());
        userRepository.save(user);

        return user.getPoints();
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public static class PointNotAvailableException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public static class UserNotAvailableException extends RuntimeException {

    }
}
