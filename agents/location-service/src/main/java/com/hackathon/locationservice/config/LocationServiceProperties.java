package com.hackathon.locationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "location-service")
public record LocationServiceProperties(
		int nearbyRadiusKm
) {
	public LocationServiceProperties() {
		this(200);
	}
}
