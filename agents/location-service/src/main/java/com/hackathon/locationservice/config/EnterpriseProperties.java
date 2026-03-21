package com.hackathon.locationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "location-service")
public record EnterpriseProperties(
		String enterpriseBaseUrl
) {
	public EnterpriseProperties() {
		this("http://localhost:8085");
	}

	public String baseUrl() {
		return enterpriseBaseUrl != null ? enterpriseBaseUrl : "http://localhost:8085";
	}
}
