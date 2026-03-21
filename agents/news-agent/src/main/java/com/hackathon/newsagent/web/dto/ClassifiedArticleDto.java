package com.hackathon.newsagent.web.dto;

import java.util.List;

public record ClassifiedArticleDto(
        String uri,
        String title,
        String body,
        String url,
        String date,
        String dateTime,
        List<CategoryAssignmentDto> categories,
        /** Short narrative: top themes + excerpt of the article text. */
        String summary,
        ShippingRouteImpactDto shippingRouteImpact
) {
}
