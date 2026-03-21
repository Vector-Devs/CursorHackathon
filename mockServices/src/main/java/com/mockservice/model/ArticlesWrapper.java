package com.mockservice.model;

import java.util.List;

public record ArticlesWrapper(
    int count,
    int page,
    int pages,
    int totalResults,
    List<Article> results
) {}
