package com.hackathon.locationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlantDto(
		Long id,
		String plantName,
		String location,
		String latitude,
		String longitude
) {}
