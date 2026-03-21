package com.example.demo.controller;

import com.example.demo.model.Vessel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vessels_operations")
public class VesselController {

    @Value("classpath:mock_vessels.json")
    private Resource mockVesselsResource;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/get-vessels-by-area", produces = "application/json")
    public List<Vessel> getVessels(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double circle_radius
    ) throws Exception {
        
        if (!mockVesselsResource.exists()) {
            throw new IllegalStateException("mock_vessels.json not found in classpath");
        }

        List<Vessel> allVessels = objectMapper.readValue(
                mockVesselsResource.getInputStream(),
                new TypeReference<List<Vessel>>() {}
        );

        // If no parameters provided, return the default vessels from the prompt
        // which are at lat ~45, lon ~ -8.8
        if (latitude == null || longitude == null || circle_radius == null) {
            return allVessels.stream()
                    .filter(v -> v.name().equals("ELBEBORG") || 
                                v.name().equals("MARCUS") || 
                                v.name().equals("WANHEIM") || 
                                v.name().equals("CGAS JAGUAR"))
                    .collect(Collectors.toList());
        }

        // Filter by radius
        return allVessels.stream()
                .filter(vessel -> {
                    try {
                        double vLat = Double.parseDouble(vessel.latitude());
                        double vLon = Double.parseDouble(vessel.longitude());
                        return haversine(latitude, longitude, vLat, vLon) <= circle_radius;
                    } catch (NumberFormatException e) {
                        return false; // Skip if invalid coordinates
                    }
                })
                .collect(Collectors.toList());
    }

    // Haversine formula to calculate distance in Kilometers
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // returns km
    }
}
