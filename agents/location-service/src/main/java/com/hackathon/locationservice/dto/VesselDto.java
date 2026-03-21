package com.hackathon.locationservice.dto;

public record VesselDto(
		String mmsi,
		String name,
		String latitude,
		String longitude,
		String speed,
		String course,
		String heading
) {}
