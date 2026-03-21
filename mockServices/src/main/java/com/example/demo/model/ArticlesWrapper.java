package com.example.demo.model;

import java.util.List;

public record ArticlesWrapper(
    int count,
    int page,
    int pages,
    int totalResults,
    List<Article> results
) {}
