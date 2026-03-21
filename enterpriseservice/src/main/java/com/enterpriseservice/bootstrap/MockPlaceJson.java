package com.enterpriseservice.bootstrap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Shape matches {@code mockServices} {@code mock_places.json} (kept in sync on classpath).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MockPlaceJson(String name, String type, String latitude, String longitude) {
}
