package com.enterpriseservice.web;

import com.enterpriseservice.domain.Plant;
import com.enterpriseservice.service.MasterDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plants")
@RequiredArgsConstructor
public class PlantController {

	private final MasterDataService masterDataService;

	@GetMapping
	public List<Plant> list() {
		return masterDataService.listPlants();
	}

	@GetMapping("/{id}")
	public Plant get(@PathVariable Long id) {
		return masterDataService.getPlant(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Plant create(@Valid @RequestBody Plant body) {
		return masterDataService.createPlant(body);
	}

	@PutMapping("/{id}")
	public Plant update(@PathVariable Long id, @RequestBody Plant body) {
		return masterDataService.updatePlant(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		masterDataService.deletePlant(id);
	}
}
