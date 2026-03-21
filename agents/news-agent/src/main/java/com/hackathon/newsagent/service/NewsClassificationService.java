package com.hackathon.newsagent.service;

import com.hackathon.newsagent.classification.ArticleClassifier;
import com.hackathon.newsagent.classification.CategoryScore;
import com.hackathon.newsagent.classification.ShippingRouteImpactScorer;
import com.hackathon.newsagent.client.NewsApiClient;
import com.hackathon.newsagent.config.ClassificationProperties;
import com.hackathon.newsagent.model.news.ArticleJson;
import com.hackathon.newsagent.summary.ArticleSummaryComposer;
import com.hackathon.newsagent.web.dto.AgentRunResponse;
import com.hackathon.newsagent.web.dto.CategoryAssignmentDto;
import com.hackathon.newsagent.web.dto.ClassifiedArticleDto;
import com.hackathon.newsagent.web.dto.ShippingRouteImpactDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsClassificationService {

    private static final int TOP_CATEGORY_COUNT = 4;

    private final NewsApiClient newsApiClient;
    private final ArticleClassifier classifier;
    private final ShippingRouteImpactScorer shippingRouteImpactScorer;
    private final ClassificationProperties classificationProps;

    public NewsClassificationService(
            NewsApiClient newsApiClient,
            ArticleClassifier classifier,
            ShippingRouteImpactScorer shippingRouteImpactScorer,
            ClassificationProperties classificationProps
    ) {
        this.newsApiClient = newsApiClient;
        this.classifier = classifier;
        this.shippingRouteImpactScorer = shippingRouteImpactScorer;
        this.classificationProps = classificationProps;
    }

    public AgentRunResponse runAgent() {
        List<ArticleJson> articles = newsApiClient.fetchArticles();
        List<ClassifiedArticleDto> classified = new ArrayList<>();
        for (ArticleJson article : articles) {
            List<CategoryScore> scores = classifier.classify(article.title(), article.body());
            int cap = Math.min(TOP_CATEGORY_COUNT, classificationProps.maxCategoriesPerArticle());
            List<CategoryAssignmentDto> assignments = pickTopCategories(scores, cap);
            String summary = ArticleSummaryComposer.compose(
                    article.title(),
                    article.body(),
                    assignments
            );
            ShippingRouteImpactDto routeImpact =
                    shippingRouteImpactScorer.score(article.title(), article.body());
            classified.add(new ClassifiedArticleDto(
                    article.uri(),
                    article.title(),
                    article.body(),
                    article.url(),
                    article.date(),
                    article.dateTime(),
                    assignments,
                    summary,
                    routeImpact
            ));
        }
        return new AgentRunResponse(articles.size(), classified);
    }

    /**
     * Takes the highest-scoring categories (probabilities already ordered by {@link ArticleClassifier}).
     * min-score is not applied when ranking the top N so the UI always shows the four strongest signals when available.
     */
    private List<CategoryAssignmentDto> pickTopCategories(List<CategoryScore> scores, int cap) {
        List<CategoryAssignmentDto> out = new ArrayList<>();
        for (CategoryScore s : scores) {
            if (out.size() >= cap) {
                break;
            }
            out.add(toAssignment(s));
        }
        return out;
    }

    private CategoryAssignmentDto toAssignment(CategoryScore s) {
        return new CategoryAssignmentDto(
                s.category().name(),
                s.category().displayName(),
                s.category().description(),
                s.score(),
                s.matchedSignals()
        );
    }
}
