package com.example.demo.model;

public record Place(
    String name,
    String type, // PORT, CITY, COUNTRY, CHOKEPOINT
    String latitude,
    String longitude
) {}
