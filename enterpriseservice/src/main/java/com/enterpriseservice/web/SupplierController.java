package com.enterpriseservice.web;

import com.enterpriseservice.domain.Supplier;
import com.enterpriseservice.service.SupplierCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Read-only suppliers API backed by mock_suppliers.json. Gulf region cities only.
 * Suppliers are linked to European plants.
 */
@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

	private final SupplierCatalogService supplierCatalogService;

	@GetMapping
	public List<Supplier> list() {
		return supplierCatalogService.listAll();
	}

	@GetMapping("/{id}")
	public Supplier get(@PathVariable Long id) {
		return supplierCatalogService.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
	}
}
