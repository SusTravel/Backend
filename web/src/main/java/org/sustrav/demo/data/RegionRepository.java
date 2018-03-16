package org.sustrav.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sustrav.demo.data.model.Region;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, String> {
    List<Region> findAllByContinentCode(String continentCode);
}
