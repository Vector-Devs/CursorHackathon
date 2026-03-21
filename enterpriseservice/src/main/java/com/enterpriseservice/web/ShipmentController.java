package com.enterpriseservice.web;

import com.enterpriseservice.domain.Shipment;
import com.enterpriseservice.service.MasterDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

	private final MasterDataService masterDataService;

	@GetMapping
	public List<Shipment> list() {
		return masterDataService.listShipments();
	}

	@GetMapping("/{id}")
	public Shipment get(@PathVariable Long id) {
		return masterDataService.getShipment(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Shipment create(@Valid @RequestBody Shipment body) {
		return masterDataService.createShipment(body);
	}

	@PutMapping("/{id}")
	public Shipment update(@PathVariable Long id, @RequestBody Shipment body) {
		return masterDataService.updateShipment(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		masterDataService.deleteShipment(id);
	}
}
