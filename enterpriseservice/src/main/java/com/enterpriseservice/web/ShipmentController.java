package com.enterpriseservice.web;

import com.enterpriseservice.domain.Shipment;
import com.enterpriseservice.service.ShipmentCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Read-only shipments API backed by mock_shipments.json. Links plants and suppliers.
 */
@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

	private final ShipmentCatalogService shipmentCatalogService;

	@GetMapping
	public List<Shipment> list() {
		return shipmentCatalogService.listAll();
	}

	@GetMapping("/{id}")
	public Shipment get(@PathVariable Long id) {
		return shipmentCatalogService.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));
	}
}
