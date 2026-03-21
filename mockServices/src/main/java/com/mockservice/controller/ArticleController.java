package com.mockservice.controller;

import com.mockservice.service.MockArticlesDynamicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    private final MockArticlesDynamicService dynamicArticles;

    public ArticleController(MockArticlesDynamicService dynamicArticles) {
        this.dynamicArticles = dynamicArticles;
    }

    /**
     * Returns the same schema as {@code mock_articles.json}, with titles/bodies/timestamps/sentiment
     * adjusted on each call so demos can poll every second and see gradual change.
     */
    @PostMapping(value = "/getArticles", produces = "application/json")
    public String getArticles() throws Exception {
        return dynamicArticles.buildJsonResponse();
    }
}
