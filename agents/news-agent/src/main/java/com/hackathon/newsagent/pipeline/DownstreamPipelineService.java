package com.hackathon.newsagent.pipeline;

import com.hackathon.newsagent.config.PipelineProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Chains: news-agent (scheduled) → reasoning-agent → supply-chain-risk (simulation).
 * Each step is a GET that recomputes; the UI then reads the same simulation endpoint.
 */
@Service
@ConditionalOnProperty(name = "agent.pipeline.enabled", havingValue = "true", matchIfMissing = true)
public class DownstreamPipelineService {

    private static final Logger log = LoggerFactory.getLogger(DownstreamPipelineService.class);

    private final RestClient pipelineRestClient;
    private final PipelineProperties props;
    private final AtomicLong lastCascadeStartMs = new AtomicLong(0L);

    public DownstreamPipelineService(
            @Qualifier("pipeline") RestClient pipelineRestClient,
            PipelineProperties props
    ) {
        this.pipelineRestClient = pipelineRestClient;
        this.props = props;
    }

    /**
     * Invoked after a successful in-process news classification run (scheduled path).
     */
    public void refreshReasoningThenSimulation() {
        if (!props.enabled()) {
            return;
        }
        long minMs = props.minIntervalMs();
        if (minMs > 0) {
            long now = System.currentTimeMillis();
            long last = lastCascadeStartMs.get();
            if (now - last < minMs) {
                log.trace("Pipeline: skipped (min interval {} ms not elapsed)", minMs);
                return;
            }
            if (!lastCascadeStartMs.compareAndSet(last, now)) {
                return;
            }
        }

        String reasoningUri = UriComponentsBuilder.fromUriString(props.reasoningBaseUrl())
                .path("/api/agent/reasoning-report")
                .build()
                .toUriString();
        String simulationUri = UriComponentsBuilder.fromUriString(props.simulationBaseUrl())
                .path("/api/agent/supply-chain-risk-report")
                .build()
                .toUriString();

        try {
            ResponseEntity<Void> r1 = pipelineRestClient.get()
                    .uri(reasoningUri)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Pipeline: reasoning-agent OK (status {})", r1.getStatusCode());
        } catch (Exception e) {
            log.warn("Pipeline: reasoning-agent failed: {}", e.toString());
            return;
        }

        try {
            ResponseEntity<Void> r2 = pipelineRestClient.get()
                    .uri(simulationUri)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Pipeline: simulation agent OK (status {})", r2.getStatusCode());
        } catch (Exception e) {
            log.warn("Pipeline: simulation agent failed: {}", e.toString());
        }
    }
}
