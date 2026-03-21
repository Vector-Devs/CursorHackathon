package com.enterpriseservice.repository;

import com.enterpriseservice.domain.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

	@EntityGraph(attributePaths = {"suppliers", "plants"})
	@Query("select sh from Shipment sh where sh.id = :id")
	Optional<Shipment> findDetailById(@Param("id") Long id);
}
