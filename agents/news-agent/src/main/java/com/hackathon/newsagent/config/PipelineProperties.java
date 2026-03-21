package com.hackathon.newsagent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * After each scheduled news run, optionally GET reasoning-agent then supply-chain-risk (simulation)
 * so the demo stack stays warm end-to-end.
 */
@ConfigurationProperties(prefix = "agent.pipeline")
public record PipelineProperties(
        /** When true, cascade to reasoning then simulation after each scheduled news run. */
        boolean enabled,
        String reasoningBaseUrl,
        String simulationBaseUrl,
        /** HTTP read timeout for downstream GETs (ms); simulation can be slow. */
        int readTimeoutMs,
        /** Connect timeout (ms). */
        int connectTimeoutMs,
        /**
         * Minimum milliseconds between cascade runs. When the news schedule is faster (e.g. 1s), use
         * this to avoid hammering reasoning/simulation. 0 = cascade after every scheduled news run.
         */
        long minIntervalMs
) {
    public PipelineProperties {
        if (reasoningBaseUrl == null || reasoningBaseUrl.isBlank()) {
            reasoningBaseUrl = "http://localhost:8093";
        }
        if (simulationBaseUrl == null || simulationBaseUrl.isBlank()) {
            simulationBaseUrl = "http://localhost:8094";
        }
        if (readTimeoutMs <= 0) {
            readTimeoutMs = 120_000;
        }
        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = 10_000;
        }
        if (minIntervalMs < 0) {
            minIntervalMs = 0;
        }
    }
}
