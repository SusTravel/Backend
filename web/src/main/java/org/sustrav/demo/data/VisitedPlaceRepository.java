package org.sustrav.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.sustrav.demo.data.model.VisitedPlace;

import java.util.List;

public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, String> {

    @Modifying
    @Query("delete from VisitedPlace vp where vp.user.id= ?1")
    void deleteVisitedPlaceForUser(Long userId);

    List<VisitedPlace> findAllByUserId(Long userId);
}
