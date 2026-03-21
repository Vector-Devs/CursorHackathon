package com.enterpriseservice.bootstrap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO for deserializing supplier records from mock_suppliers.json.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MockSupplierJson(
		String supplierName,
		String location,
		String latitude,
		String longitude,
		String contractStatus,
		List<Long> plantIds
) {}
