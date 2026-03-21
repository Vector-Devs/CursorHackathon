package com.enterpriseservice.service;

import com.enterpriseservice.bootstrap.MockSupplierJson;
import com.enterpriseservice.domain.Plant;
import com.enterpriseservice.domain.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Read-only catalog of suppliers from mock_suppliers.json. Gulf region cities only.
 * Links suppliers to European plants. Same pattern as PlantCatalogService.
 */
@Service
@RequiredArgsConstructor
public class SupplierCatalogService {

	@Value("classpath:mock_suppliers.json")
	private Resource mockSuppliersResource;

	private final JsonMapper jsonMapper;
	private final PlantCatalogService plantCatalogService;

	private List<Supplier> suppliers = new ArrayList<>();

	@PostConstruct
	public void loadCatalog() throws IOException {
		if (!mockSuppliersResource.exists()) {
			throw new IllegalStateException("mock_suppliers.json not found in classpath");
		}
		List<MockSupplierJson> raw = jsonMapper.readValue(
				mockSuppliersResource.getInputStream(),
				new TypeReference<List<MockSupplierJson>>() {});
		suppliers = new ArrayList<>();
		for (int i = 0; i < raw.size(); i++) {
			MockSupplierJson s = raw.get(i);
			if (s.supplierName() == null || s.supplierName().isBlank()) continue;

			Set<Plant> linkedPlants = new HashSet<>();
			if (s.plantIds() != null) {
				for (Long plantId : s.plantIds()) {
					plantCatalogService.getById(plantId).ifPresent(linkedPlants::add);
				}
			}

			Supplier supplier = Supplier.builder()
					.id((long) (i + 1))
					.supplierName(s.supplierName())
					.location(s.location())
					.latitude(s.latitude())
					.longitude(s.longitude())
					.contractStatus(s.contractStatus() != null ? s.contractStatus() : "ACTIVE")
					.plants(linkedPlants)
					.build();

			// Reverse link: add this supplier to each plant's suppliers
			for (Plant plant : linkedPlants) {
				plant.getSuppliers().add(supplier);
			}
			suppliers.add(supplier);
		}
	}

	public List<Supplier> listAll() {
		return new ArrayList<>(suppliers);
	}

	public Optional<Supplier> getById(Long id) {
		int idx = id != null ? id.intValue() - 1 : -1;
		if (idx >= 0 && idx < suppliers.size()) {
			return Optional.of(suppliers.get(idx));
		}
		return Optional.empty();
	}
}
