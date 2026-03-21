package com.hackathon.locationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SupplierDto(
		Long id,
		String supplierName,
		String location,
		String latitude,
		String longitude
) {}
