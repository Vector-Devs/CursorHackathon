package com.hackathon.newsagent.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties({NewsApiProperties.class, ClassificationProperties.class, PipelineProperties.class})
public class AppConfig {

    @Bean
    RestClient newsRestClient(NewsApiProperties props) {
        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .build();
    }

    /**
     * Long timeouts: reasoning + simulation calls can exceed default client limits during demos.
     */
    @Bean
    @Qualifier("pipeline")
    RestClient pipelineRestClient(PipelineProperties pipelineProperties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(pipelineProperties.connectTimeoutMs()));
        factory.setReadTimeout(Duration.ofMillis(pipelineProperties.readTimeoutMs()));
        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
