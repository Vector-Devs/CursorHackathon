package com.hackathon.locationservice.web;

import com.hackathon.locationservice.service.LocationAggregationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

	private final LocationAggregationService locationAggregationService;

	public LocationController(LocationAggregationService locationAggregationService) {
		this.locationAggregationService = locationAggregationService;
	}

	/**
	 * Returns unique cities from plants, suppliers, and nearby vessel positions.
	 */
	@GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getUniqueCities() {
		return locationAggregationService.getUniqueCities();
	}

	/**
	 * Returns full location summary: unique cities, plants, and suppliers.
	 */
	@GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
	public LocationAggregationService.LocationSummary getLocationSummary() {
		return locationAggregationService.getLocationSummary();
	}
}
