package com.hackathon.probability.web;

import com.hackathon.probability.dto.ProbabilityResponse;
import com.hackathon.probability.service.ProbabilityPushService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/probability")
public class ProbabilityController {

	private final ProbabilityPushService pushService;

	public ProbabilityController(ProbabilityPushService pushService) {
		this.pushService = pushService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProbabilities() {
		ProbabilityResponse cached = pushService.getLatest();
		if (cached != null) {
			return ResponseEntity.ok(cached);
		}
		return ResponseEntity.status(503).body(Map.of("message", "No probability data yet; check back shortly"));
	}
}
