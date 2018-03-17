package org.sustrav.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.sustrav.demo.data.PlaceRepository;
import org.sustrav.demo.data.UserRepository;
import org.sustrav.demo.data.VisitedPlaceRepository;
import org.sustrav.demo.data.model.Place;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.data.model.UserRole;
import org.sustrav.demo.services.BillingService;
import org.sustrav.demo.services.user.UserAuthentication;
import org.sustrav.demo.services.user.UserService;
import org.sustrav.demo.utils.GeoHandler;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional()
@Commit
public class SimpleDatabaseTests {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BillingService billingService;

    @Autowired
    private UserService userService;

    @Autowired
    private VisitedPlaceRepository visitedPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void simpleUseCase() {
        User user = userService.createInternalUser("test", UserRole.USER);
        SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user));

        List<Place> results = placeRepository.findNearestPlaces(GeoHandler.createCircle(10D, -10D, 5), user.getId(), 100);
        assertThat(results).isNotEmpty();
        Place place = results.get(0);

        results = placeRepository.findNearestPlaces(GeoHandler.createCircle(20D, -20D, 5), user.getId(), 100);
        assertThat(results).isEmpty();

        billingService.acceptPoint(place.getId(), place.getLocation().getX(), place.getLocation().getY());
        results = placeRepository.findNearestPlaces(GeoHandler.createCircle(10D, -10D, 5), user.getId(), 100);
        assertThat(results).isNotEmpty();

        billingService.acceptPoint(results.get(0).getId(), place.getLocation().getX(), place.getLocation().getY());
        results = placeRepository.findNearestPlaces(GeoHandler.createCircle(10D, -10D, 5), user.getId(), 100);
        assertThat(results).isEmpty();
    }

    private void persist(Object obj) {
        em.persist(obj);
        em.flush();
    }
}
