package com.mockservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Value("classpath:mock_articles.json")
    private Resource mockArticlesResource;

    @PostMapping(value = "/getArticles", produces = "application/json")
    public String getArticles() throws Exception {
        if (mockArticlesResource.exists()) {
            return new String(mockArticlesResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IllegalStateException("mock_articles.json not found in classpath");
        }
    }
}
