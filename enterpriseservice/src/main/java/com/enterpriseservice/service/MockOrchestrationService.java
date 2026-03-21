package com.enterpriseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Stateless aggregation of mock articles, vessels, and places (parallel RestClient calls).
 */
@Service
@RequiredArgsConstructor
public class MockOrchestrationService {

	private final RestClient mockServicesRestClient;
	private final ExecutorService orchestrationExecutor = Executors.newVirtualThreadPerTaskExecutor();

	public Map<String, Object> snapshot(Double latitude, Double longitude, Double vesselRadiusKm) {
		double lat = latitude != null ? latitude : 25.26;
		double lon = longitude != null ? longitude : 55.30;

		CompletableFuture<String> articles = CompletableFuture.supplyAsync(this::fetchArticles, orchestrationExecutor);
		CompletableFuture<String> vessels = CompletableFuture.supplyAsync(
				() -> fetchVessels(latitude, longitude, vesselRadiusKm), orchestrationExecutor);
		CompletableFuture<String> places = CompletableFuture.supplyAsync(() -> fetchPlaces(lat, lon), orchestrationExecutor);

		Map<String, Object> out = new LinkedHashMap<>();
		try {
			out.put("articles", articles.join());
			out.put("vessels", vessels.join());
			out.put("places", places.join());
		} catch (Exception e) {
			out.put("error", e.getMessage());
		}
		return out;
	}

	private String fetchArticles() {
		try {
			return mockServicesRestClient.post()
					.uri("/api/v1/article/getArticles")
					.contentType(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(String.class);
		} catch (RestClientException e) {
			return "{\"error\":\"" + escape(e.getMessage()) + "\"}";
		}
	}

	private String fetchVessels(Double latitude, Double longitude, Double vesselRadiusKm) {
		try {
			return mockServicesRestClient.post()
					.uri((UriBuilder uriBuilder) -> {
						var b = uriBuilder.path("/api/vessels_operations/get-vessels-by-area");
						if (latitude != null && longitude != null) {
							b.queryParam("latitude", latitude).queryParam("longitude", longitude);
							if (vesselRadiusKm != null) {
								b.queryParam("circle_radius", vesselRadiusKm);
							}
						}
						return b.build();
					})
					.contentType(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(String.class);
		} catch (RestClientException e) {
			return "{\"error\":\"" + escape(e.getMessage()) + "\"}";
		}
	}

	private String fetchPlaces(double latitude, double longitude) {
		try {
			return mockServicesRestClient.post()
					.uri((UriBuilder uriBuilder) -> uriBuilder
							.path("/api/vessels_operations/get-places")
							.queryParam("latitude", latitude)
							.queryParam("longitude", longitude)
							.build())
					.contentType(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(String.class);
		} catch (RestClientException e) {
			return "{\"error\":\"" + escape(e.getMessage()) + "\"}";
		}
	}

	private static String escape(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("\"", "'");
	}
}
