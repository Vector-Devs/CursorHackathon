package com.enterpriseservice.bootstrap;

import com.enterpriseservice.domain.Plant;
import com.enterpriseservice.repository.PlantRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Seeds {@link Plant} rows from {@code classpath:mock_places.json} when the plant table is empty,
 * so GET /api/v1/plants reflects the same geography mock data as {@code mockServices}.
 */
@Service
@RequiredArgsConstructor
public class MockPlacesPlantSeedService {

	private final PlantRepo plantRepo;
	private final ObjectMapper objectMapper;

	@Value("classpath:mock_places.json")
	private Resource mockPlacesResource;

	@Transactional
	public void seedIfEmpty() throws IOException {
		if (plantRepo.count() > 0) {
			return;
		}
		if (!mockPlacesResource.exists()) {
			return;
		}
		List<MockPlaceJson> places = objectMapper.readValue(
				mockPlacesResource.getInputStream(),
				new TypeReference<List<MockPlaceJson>>() {
				});
		List<Plant> batch = new ArrayList<>();
		for (MockPlaceJson p : places) {
			if (p.name() == null || p.name().isBlank()) {
				continue;
			}
			batch.add(Plant.builder()
					.plantName(p.name())
					.location(p.type() != null ? p.type() : "PLACE")
					.latitude(p.latitude())
					.longitude(p.longitude())
					.status("ACTIVE")
					.build());
		}
		plantRepo.saveAll(batch);
	}
}
