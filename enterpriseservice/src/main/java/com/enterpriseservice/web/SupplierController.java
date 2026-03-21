package com.enterpriseservice.web;

import com.enterpriseservice.domain.Supplier;
import com.enterpriseservice.service.MasterDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

	private final MasterDataService masterDataService;

	@GetMapping
	public List<Supplier> list() {
		return masterDataService.listSuppliers();
	}

	@GetMapping("/{id}")
	public Supplier get(@PathVariable Long id) {
		return masterDataService.getSupplier(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Supplier create(@Valid @RequestBody Supplier body) {
		return masterDataService.createSupplier(body);
	}

	@PutMapping("/{id}")
	public Supplier update(@PathVariable Long id, @RequestBody Supplier body) {
		return masterDataService.updateSupplier(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		masterDataService.deleteSupplier(id);
	}
}
