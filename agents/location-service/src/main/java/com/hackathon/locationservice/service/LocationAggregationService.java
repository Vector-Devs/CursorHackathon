package com.hackathon.locationservice.service;

import com.hackathon.locationservice.client.EnterpriseClient;
import com.hackathon.locationservice.client.MockServicesClient;
import com.hackathon.locationservice.config.LocationServiceProperties;
import com.hackathon.locationservice.dto.PlantDto;
import com.hackathon.locationservice.dto.ShipmentDto;
import com.hackathon.locationservice.dto.SupplierDto;
import com.hackathon.locationservice.dto.VesselDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class LocationAggregationService {

	private final EnterpriseClient enterpriseClient;
	private final MockServicesClient mockServicesClient;
	private final int nearbyRadiusKm;
	private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

	public LocationAggregationService(EnterpriseClient enterpriseClient,
			MockServicesClient mockServicesClient,
			LocationServiceProperties properties) {
		this.enterpriseClient = enterpriseClient;
		this.mockServicesClient = mockServicesClient;
		this.nearbyRadiusKm = properties.nearbyRadiusKm();
	}

	public LocationSummary getLocationSummary() {
		CompletableFuture<List<PlantDto>> plantsFuture = CompletableFuture.supplyAsync(enterpriseClient::listPlants, executor);
		CompletableFuture<List<SupplierDto>> suppliersFuture = CompletableFuture.supplyAsync(enterpriseClient::listSuppliers, executor);
		CompletableFuture<List<ShipmentDto>> shipmentsFuture = CompletableFuture.supplyAsync(enterpriseClient::listShipments, executor);

		CompletableFuture.allOf(plantsFuture, suppliersFuture, shipmentsFuture).join();

		List<PlantDto> plants = plantsFuture.join();
		List<SupplierDto> suppliers = suppliersFuture.join();
		List<ShipmentDto> shipments = shipmentsFuture.join();

		Set<String> cities = new HashSet<>();

		// Add plant locations (location string as city name when present)
		plants.stream()
				.map(PlantDto::location)
				.filter(loc -> loc != null && !loc.isBlank())
				.forEach(cities::add);

		// Add supplier locations
		suppliers.stream()
				.map(SupplierDto::location)
				.filter(loc -> loc != null && !loc.isBlank())
				.forEach(cities::add);

		// Get ship names for non-delivered shipments
		Set<String> shipNames = shipments.stream()
				.filter(s -> s.status() != null && !"DELIVERED".equalsIgnoreCase(s.status()))
				.map(ShipmentDto::shipNumber)
				.filter(n -> n != null && !n.isBlank())
				.collect(Collectors.toSet());

		// Resolve vessel positions and find nearby cities via MockServices nearby-cities endpoint
		if (!shipNames.isEmpty()) {
			List<VesselDto> vessels = mockServicesClient.getVesselsByNames(shipNames);
			for (VesselDto vessel : vessels) {
				double vLat = parseDouble(vessel.latitude());
				double vLon = parseDouble(vessel.longitude());
				if (Double.isNaN(vLat) || Double.isNaN(vLon)) continue;

				List<String> nearbyCityNames = mockServicesClient.getNearbyCities(vLat, vLon);
				cities.addAll(nearbyCityNames);
			}
		}

		List<String> sortedCities = cities.stream()
				.sorted()
				.toList();

		return new LocationSummary(sortedCities, plants, suppliers);
	}

	public List<String> getUniqueCities() {
		return getLocationSummary().cities();
	}

	private static double parseDouble(String s) {
		if (s == null || s.isBlank()) return Double.NaN;
		try {
			return Double.parseDouble(s.trim());
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	public record LocationSummary(
			List<String> cities,
			List<PlantDto> plants,
			List<SupplierDto> suppliers
	) {}
}
