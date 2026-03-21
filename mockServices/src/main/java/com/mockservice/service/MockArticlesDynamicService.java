package com.mockservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Serves the static {@code mock_articles.json} shape with fields that drift gradually over time so
 * demos can poll every second and see continuous change without random jumps.
 */
@Service
public class MockArticlesDynamicService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Resource mockArticlesResource;
    private JsonNode cachedRoot;

    public MockArticlesDynamicService(@Value("classpath:mock_articles.json") Resource mockArticlesResource) {
        this.mockArticlesResource = mockArticlesResource;
    }

    @PostConstruct
    void loadBaseJson() throws IOException {
        if (!mockArticlesResource.exists()) {
            throw new IllegalStateException("mock_articles.json not found in classpath");
        }
        try (InputStream in = mockArticlesResource.getInputStream()) {
            cachedRoot = MAPPER.readTree(in);
        }
    }

    public String buildJsonResponse() throws IOException {
        JsonNode root = cachedRoot.deepCopy();
        JsonNode resultsNode = root.path("articles").path("results");
        if (!resultsNode.isArray()) {
            return MAPPER.writeValueAsString(root);
        }

        Instant now = Instant.now();
        double t = now.getEpochSecond() + now.getNano() / 1_000_000_000.0;
        String iso = DateTimeFormatter.ISO_INSTANT.format(now);

        ArrayNode results = (ArrayNode) resultsNode;
        for (int i = 0; i < results.size(); i++) {
            ObjectNode article = (ObjectNode) results.get(i);
            // Slow drift (minutes) + gentle per-second motion so adjacent polls differ slightly.
            double drift = Math.sin(t / 90.0 + i * 0.5) + 0.5 * Math.sin(t / 12.0 + i * 0.3);
            double wiggle = 0.12 * Math.sin(t * 2.0 * Math.PI + i * 0.7);
            double stress = 0.5 + 0.25 * drift + wiggle;
            stress = Math.max(0.0, Math.min(1.0, stress));

            String baseTitle = article.path("title").asText();
            article.put("title", baseTitle + String.format(Locale.US, " [demo %.3f]", stress));

            String baseBody = article.path("body").asText();
            String watch = stress > 0.65 ? "elevated" : stress > 0.35 ? "watch" : "stable";
            String demoLine = String.format(
                    Locale.US,
                    "\n\n[Demo signal] Stress index %.3f (gradual). Watchlist: %s.",
                    stress,
                    watch);
            article.put("body", baseBody + demoLine);

            article.put("dateTime", iso);
            article.put("dateTimePub", iso);
            String datePart = iso.length() >= 10 ? iso.substring(0, 10) : "";
            article.put("date", datePart);
            String timePart = iso.length() >= 19 ? iso.substring(11, 19) : "";
            article.put("time", timePart);

            if (article.has("sentiment")) {
                article.put("sentiment", -0.15 + 0.3 * (stress - 0.5));
            }
            if (article.has("relevance")) {
                article.put("relevance", (int) Math.round(80 + 40 * stress));
            }
        }
        return MAPPER.writeValueAsString(root);
    }
}
