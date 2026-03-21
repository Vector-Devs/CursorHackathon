package com.mockservice.model;

public record Vessel(
    String mmsi,
    String name,
    String latitude,
    String longitude,
    String speed,
    String course,
    String heading
) {}
