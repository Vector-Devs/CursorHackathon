package com.mockservice.model;

public record Author(
    String uri,
    String name,
    String type,
    boolean isAgency
) {}
