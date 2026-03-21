package com.hackathon.newsagent.service;

import com.hackathon.newsagent.ai.NewsClassificationResult;
import com.hackathon.newsagent.ai.NewsClassifierAiService;
import com.hackathon.newsagent.client.LocationServiceClient;
import com.hackathon.newsagent.client.MockServicesClient;
import com.hackathon.newsagent.dto.ArticleDto;
import com.hackathon.newsagent.dto.ArticlesResponseDto;
import com.hackathon.newsagent.dto.ClassifiedArticleDto;
import com.hackathon.newsagent.dto.ClassifiedNewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;

@Service
public class NewsClassificationService {

	private static final Logger log = LoggerFactory.getLogger(NewsClassificationService.class);

	private static final Pattern DISPATCH_SUFFIX = Pattern.compile("\\s*[—\\-]\\s*dispatch\\s+\\d+\\s*", Pattern.CASE_INSENSITIVE);
	private static final Pattern DEMO_SUFFIX = Pattern.compile("\\s*\\[demo\\s+[\\d.]+\\]\\s*$");

	private static final int DEFAULT_CLASSIFICATION_THREADS = 2;

	private final MockServicesClient mockServicesClient;
	private final LocationServiceClient locationServiceClient;
	private final NewsClassifierAiService newsClassifierAiService;
	private final ExecutorService classificationExecutor;

	public NewsClassificationService(
			MockServicesClient mockServicesClient,
			LocationServiceClient locationServiceClient,
			NewsClassifierAiService newsClassifierAiService,
			@Value("${news.classification-threads:" + DEFAULT_CLASSIFICATION_THREADS + "}") int classificationThreads) {
		this.mockServicesClient = mockServicesClient;
		this.locationServiceClient = locationServiceClient;
		this.newsClassifierAiService = newsClassifierAiService;
		int threads = classificationThreads > 0 ? classificationThreads : DEFAULT_CLASSIFICATION_THREADS;
		this.classificationExecutor = Executors.newFixedThreadPool(threads);
		log.info("Classification thread pool size: {} (stays under Anthropic 50 req/min limit)", threads);
	}

	public ClassifiedNewsResponse getClassifiedNews() {
		List<String> cities = locationServiceClient.getCities()
				.onErrorReturn(List.of())
				.block();

		log.info("Location-service output: count={}, cities={}", cities != null ? cities.size() : 0, cities);

		ArticlesResponseDto articlesResponse = mockServicesClient.getArticles()
				.block();

		if (articlesResponse == null || articlesResponse.articles() == null
				|| articlesResponse.articles().results() == null) {
			return new ClassifiedNewsResponse(0, List.of());
		}

		List<ArticleDto> articles = articlesResponse.articles().results();
		String citiesStr = cities != null ? String.join(", ", cities) : "";
		if (cities == null || cities.isEmpty()) {
			log.info("Location-service returned no cities; AI will extract location from article body only");
		}

		List<CompletableFuture<ClassifiedArticleDto>> futures = articles.stream()
				.map(article -> CompletableFuture.supplyAsync(
						() -> classifyArticle(article, citiesStr),
						classificationExecutor))
				.toList();

		List<ClassifiedArticleDto> classified = futures.stream()
				.map(CompletableFuture::join)
				.toList();

		return new ClassifiedNewsResponse(classified.size(), classified);
	}

	private ClassifiedArticleDto classifyArticle(ArticleDto article, String citiesStr) {
		try {
			NewsClassificationResult result = newsClassifierAiService.classify(
					article.title(),
					article.body() != null ? article.body() : "",
					citiesStr);

			log.info("AI output uri={}: title={}, locations={}, topics={}", article.uri(),
					result.title(), result.locations(), result.topics());

			List<String> locations = result.locations() != null ? result.locations() : List.of();
			if (locations.isEmpty()) {
				log.debug("AI returned empty locations for uri={}", article.uri());
			}

			String title = result.title() != null ? result.title() : article.title();
			return new ClassifiedArticleDto(
					article.uri(),
					stripDispatchSuffix(title),
					locations,
					result.topics() != null ? result.topics() : List.of());
		} catch (Exception e) {
			log.warn("Classification failed for uri={}: {} - no fallback", article.uri(), e.getMessage());
			return new ClassifiedArticleDto(
					article.uri(),
					stripDispatchSuffix(article.title()),
					List.of(),
					List.of());
		}
	}

	private String stripDispatchSuffix(String title) {
		if (title == null || title.isBlank()) return title;
		String cleaned = DEMO_SUFFIX.matcher(title).replaceAll("");
		cleaned = DISPATCH_SUFFIX.matcher(cleaned).replaceAll("");
		return cleaned.trim();
	}

}
