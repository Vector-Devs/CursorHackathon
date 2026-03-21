package com.enterpriseservice.service;

import com.enterpriseservice.bootstrap.MockShipmentJson;
import com.enterpriseservice.domain.Plant;
import com.enterpriseservice.domain.Shipment;
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
 * Read-only catalog of shipments from mock_shipments.json. Links plants and suppliers.
 */
@Service
@RequiredArgsConstructor
public class ShipmentCatalogService {

	@Value("classpath:mock_shipments.json")
	private Resource mockShipmentsResource;

	private final JsonMapper jsonMapper;
	private final PlantCatalogService plantCatalogService;
	private final SupplierCatalogService supplierCatalogService;

	private List<Shipment> shipments = new ArrayList<>();

	@PostConstruct
	public void loadCatalog() throws IOException {
		if (!mockShipmentsResource.exists()) {
			throw new IllegalStateException("mock_shipments.json not found in classpath");
		}
		List<MockShipmentJson> raw = jsonMapper.readValue(
				mockShipmentsResource.getInputStream(),
				new TypeReference<List<MockShipmentJson>>() {});
		shipments = new ArrayList<>();
		for (int i = 0; i < raw.size(); i++) {
			MockShipmentJson s = raw.get(i);

			Set<Supplier> linkedSuppliers = new HashSet<>();
			if (s.supplierIds() != null) {
				for (Long supplierId : s.supplierIds()) {
					supplierCatalogService.getById(supplierId).ifPresent(linkedSuppliers::add);
				}
			}

			Set<Plant> linkedPlants = new HashSet<>();
			if (s.plantIds() != null) {
				for (Long plantId : s.plantIds()) {
					plantCatalogService.getById(plantId).ifPresent(linkedPlants::add);
				}
			}

			Shipment shipment = Shipment.builder()
					.id((long) (i + 1))
					.shipmentItem(s.shipmentItem())
					.quantity(s.quantity())
					.shipNumber(s.shipNumber())
					.status(s.status() != null ? s.status() : "IN_TRANSIT")
					.receiveDate(s.receiveDate())
					.suppliers(linkedSuppliers)
					.plants(linkedPlants)
					.build();

			for (Supplier supplier : linkedSuppliers) {
				supplier.getShipments().add(shipment);
			}
			for (Plant plant : linkedPlants) {
				plant.getPlantShipments().add(shipment);
			}
			shipments.add(shipment);
		}
	}

	public List<Shipment> listAll() {
		return new ArrayList<>(shipments);
	}

	public Optional<Shipment> getById(Long id) {
		int idx = id != null ? id.intValue() - 1 : -1;
		if (idx >= 0 && idx < shipments.size()) {
			return Optional.of(shipments.get(idx));
		}
		return Optional.empty();
	}
}
