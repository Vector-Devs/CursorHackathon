package com.enterpriseservice.repository;

import com.enterpriseservice.domain.Plant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlantRepo extends JpaRepository<Plant, Long> {

	@EntityGraph(attributePaths = {"suppliers", "plantShipments"})
	@Query("select p from Plant p where p.id = :id")
	Optional<Plant> findDetailById(@Param("id") Long id);
}
