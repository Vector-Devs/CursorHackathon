package com.hackathon.locationservice.dto;

public record PlaceDto(
		String name,
		String type,
		String latitude,
		String longitude
) {}
