package com.enterpriseservice.bootstrap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for deserializing shipment records from mock_shipments.json.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MockShipmentJson(
		String shipmentItem,
		BigDecimal quantity,
		String shipNumber,
		String status,
		Instant receiveDate,
		List<Long> supplierIds,
		List<Long> plantIds
) {}
