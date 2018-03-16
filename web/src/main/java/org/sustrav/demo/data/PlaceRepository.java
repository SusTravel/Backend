package org.sustrav.demo.data;

import com.vividsolutions.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.sustrav.demo.data.model.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select p from Place p where " +
            "within(p.location, ?1) = true and " +
            "p.id not in (select vp.place.id from VisitedPlace vp where vp.user.id=?2)")
    List<Place> findNearestPlaces(Geometry geometry, Long userid);

    @Query("select p from Place p where within(p.location, ?1) = true and p.id = ?2")
    Place findPlaceInsideLocation(Geometry geometry, Long id);

}
