package com.hackathon.newsagent.schedule;

import com.hackathon.newsagent.pipeline.DownstreamPipelineService;
import com.hackathon.newsagent.service.NewsClassificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically runs the same classification pipeline as {@code GET /api/agent/classified-news},
 * then (when {@code agent.pipeline.enabled}) GETs reasoning-agent and supply-chain-risk so the
 * demo stack stays warm for the UI.
 */
@Component
@ConditionalOnProperty(name = "agent.schedule.enabled", havingValue = "true", matchIfMissing = true)
public class ClassifiedNewsScheduler {

    private static final Logger log = LoggerFactory.getLogger(ClassifiedNewsScheduler.class);

    private final NewsClassificationService classificationService;
    private final DownstreamPipelineService downstreamPipeline;

    public ClassifiedNewsScheduler(
            NewsClassificationService classificationService,
            @Autowired(required = false) DownstreamPipelineService downstreamPipeline
    ) {
        this.classificationService = classificationService;
        this.downstreamPipeline = downstreamPipeline;
    }

    @Scheduled(
            initialDelayString = "${agent.schedule.initial-delay-ms:500}",
            fixedDelayString = "${agent.schedule.fixed-delay-ms:1000}"
    )
    public void runScheduledClassification() {
        try {
            var response = classificationService.runAgent();
            log.debug("Scheduled classified-news run finished: {} articles", response.articleCount());
            if (downstreamPipeline != null) {
                downstreamPipeline.refreshReasoningThenSimulation();
            }
        } catch (Exception e) {
            log.warn("Scheduled classified-news run failed: {}", e.toString());
        }
    }
}
