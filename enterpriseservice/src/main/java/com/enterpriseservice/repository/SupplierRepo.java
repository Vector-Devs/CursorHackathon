package com.enterpriseservice.repository;

import com.enterpriseservice.domain.Supplier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {

	@EntityGraph(attributePaths = {"plants", "shipments"})
	@Query("select s from Supplier s where s.id = :id")
	Optional<Supplier> findDetailById(@Param("id") Long id);
}
