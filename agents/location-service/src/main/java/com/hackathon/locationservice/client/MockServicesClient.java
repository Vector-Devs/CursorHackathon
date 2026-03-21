package com.hackathon.locationservice.client;

import com.hackathon.locationservice.dto.PlaceDto;
import com.hackathon.locationservice.dto.VesselDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MockServicesClient {

	private final RestClient restClient;

	public MockServicesClient(@Qualifier("mockServicesRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public List<VesselDto> getVesselsByNames(Set<String> names) {
		if (names == null || names.isEmpty()) {
			return List.of();
		}
		String namesParam = String.join(",", names);
		List<VesselDto> result = restClient.get()
				.uri("/api/v1/vessels/by-names?names={names}", namesParam)
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});
		return result != null ? result : List.of();
	}

	/**
	 * Uses existing MockServices GET /api/vessels_operations/get-places to get places near coordinates.
	 * Filters for type CITY and returns city names.
	 */
	public List<String> getNearbyCities(double latitude, double longitude) {
		List<PlaceDto> places = restClient.post()
				.uri("/api/vessels_operations/get-places?latitude={latitude}&longitude={longitude}", latitude, longitude)
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});
		if (places == null) return List.of();
		return places.stream()
				.filter(p -> "CITY".equals(p.type()))
				.map(PlaceDto::name)
				.filter(n -> n != null && !n.isBlank())
				.distinct()
				.collect(Collectors.toList());
	}
}
