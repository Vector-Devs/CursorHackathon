package com.mockservice.model;

import java.util.List;

public record Article(
    String uri,
    String lang,
    boolean isDuplicate,
    String date,
    String time,
    String dateTime,
    String dateTimePub,
    String dataType,
    int sim,
    String url,
    String title,
    String body,
    Source source,
    List<Author> authors,
    String image,
    String eventUri,
    double sentiment,
    long wgt,
    int relevance
) {}
