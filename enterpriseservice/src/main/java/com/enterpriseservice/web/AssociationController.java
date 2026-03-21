package com.enterpriseservice.web;

import com.enterpriseservice.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class AssociationController {

	private final MasterDataService masterDataService;

	@PostMapping("/plants/{plantId}/suppliers/{supplierId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void linkPlantSupplier(@PathVariable Long plantId, @PathVariable Long supplierId) {
		masterDataService.linkPlantSupplier(plantId, supplierId);
	}

	@DeleteMapping("/plants/{plantId}/suppliers/{supplierId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unlinkPlantSupplier(@PathVariable Long plantId, @PathVariable Long supplierId) {
		masterDataService.unlinkPlantSupplier(plantId, supplierId);
	}

	@PostMapping("/suppliers/{supplierId}/shipments/{shipmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void linkSupplierShipment(@PathVariable Long supplierId, @PathVariable Long shipmentId) {
		masterDataService.linkSupplierShipment(supplierId, shipmentId);
	}

	@DeleteMapping("/suppliers/{supplierId}/shipments/{shipmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unlinkSupplierShipment(@PathVariable Long supplierId, @PathVariable Long shipmentId) {
		masterDataService.unlinkSupplierShipment(supplierId, shipmentId);
	}

	@PostMapping("/plants/{plantId}/shipments/{shipmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void linkPlantShipment(@PathVariable Long plantId, @PathVariable Long shipmentId) {
		masterDataService.linkPlantShipment(plantId, shipmentId);
	}

	@DeleteMapping("/plants/{plantId}/shipments/{shipmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unlinkPlantShipment(@PathVariable Long plantId, @PathVariable Long shipmentId) {
		masterDataService.unlinkPlantShipment(plantId, shipmentId);
	}
}
