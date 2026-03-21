package com.enterpriseservice.web;

import com.enterpriseservice.service.MockOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orchestration")
@RequiredArgsConstructor
public class OrchestrationController {

	private final MockOrchestrationService mockOrchestrationService;

	/**
	 * Parallel fetch of mock articles, vessels, and places (for ops / dashboards).
	 */
	@GetMapping("/snapshot")
	public Map<String, Object> snapshot(
			@RequestParam(required = false) Double latitude,
			@RequestParam(required = false) Double longitude,
			@RequestParam(required = false) Double vesselRadiusKm) {
		return mockOrchestrationService.snapshot(latitude, longitude, vesselRadiusKm);
	}
}
