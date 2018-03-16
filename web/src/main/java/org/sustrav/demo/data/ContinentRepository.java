package org.sustrav.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sustrav.demo.data.model.Continent;

public interface ContinentRepository extends JpaRepository<Continent, String> {
}
