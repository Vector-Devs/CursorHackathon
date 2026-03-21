package com.enterpriseservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class MockServicesRestClientConfig {

	@Bean
	public RestClient mockServicesRestClient(
			@Value("${mock.services.base-url}") String baseUrl,
			@Value("${mock.services.timeout-ms:10000}") int timeoutMs) {
		String root = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
		Duration d = Duration.ofMillis(timeoutMs);
		HttpClient httpClient = HttpClient.newBuilder().connectTimeout(d).build();
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(d);
		return RestClient.builder().baseUrl(root).requestFactory(requestFactory).build();
	}
}
