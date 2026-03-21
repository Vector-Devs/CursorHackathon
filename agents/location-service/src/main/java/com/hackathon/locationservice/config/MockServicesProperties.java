package com.hackathon.locationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "location-service")
public record MockServicesProperties(
		String mockServicesBaseUrl
) {
	public MockServicesProperties() {
		this("http://localhost:8082");
	}

	public String baseUrl() {
		return mockServicesBaseUrl != null ? mockServicesBaseUrl : "http://localhost:8082";
	}
}
