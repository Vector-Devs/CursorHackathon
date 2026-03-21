package com.example.demo.model;

public record Author(
    String uri,
    String name,
    String type,
    boolean isAgency
) {}
