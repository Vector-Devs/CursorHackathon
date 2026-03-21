package com.hackathon.supplychainrisk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "supplychain-risk")
public record SupplyChainRiskProperties(
        String enterpriseBaseUrl,
        String reasoningBaseUrl,
        double proximityRadiusKm,
        int httpTimeoutMs,
        /**
         * When &gt; 0, identical {@code GET /supply-chain-risk-report} calls within this window (ms)
         * return the last computed report (faster UI after the news-agent pipeline refresh).
         */
        int cacheTtlMs
) {
    public SupplyChainRiskProperties {
        if (cacheTtlMs < 0) {
            cacheTtlMs = 0;
        }
    }
}
