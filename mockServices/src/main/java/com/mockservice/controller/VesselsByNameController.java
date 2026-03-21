package com.mockservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockservice.model.Vessel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Returns vessels by name (for resolving shipment shipNumbers to positions).
 */
@RestController
@RequestMapping("/api/v1/vessels")
public class VesselsByNameController {

    @Value("classpath:mock_vessels.json")
    private Resource mockVesselsResource;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/by-names", produces = "application/json")
    public List<Vessel> getVesselsByNames(@RequestParam("names") String namesParam) {
        if (!mockVesselsResource.exists()) {
            throw new IllegalStateException("mock_vessels.json not found in classpath");
        }

        Set<String> requestedNames = parseNames(namesParam);
        if (requestedNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<Vessel> allVessels;
        try {
            allVessels = objectMapper.readValue(
                    mockVesselsResource.getInputStream(),
                    new TypeReference<List<Vessel>>() {}
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read mock_vessels.json", e);
        }

        return allVessels.stream()
                .filter(v -> v.name() != null && requestedNames.contains(v.name()))
                .toList();
    }

    private static Set<String> parseNames(String namesParam) {
        if (namesParam == null || namesParam.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(namesParam.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }
}
