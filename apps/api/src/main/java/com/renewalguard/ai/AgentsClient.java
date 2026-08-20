package com.renewalguard.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

/**
 * HTTP client used by apps/api to communicate with apps/agents.
 *
 * The FastAPI agents service is responsible for AI work only:
 * - contract clause extraction
 * - pricing benchmarking
 * - renewal analysis
 * - recommendation generation
 * - draft negotiation email generation
 *
 * apps/api remains the system of record.
 * The agents service never writes directly to PostgreSQL and never
 * performs outbound email/cancellation actions.
 */
@Component
public class AgentsClient {

    private final WebClient webClient;

    public AgentsClient(
            WebClient.Builder builder,
            @Value("${agents.base-url}") String agentsBaseUrl,
            @Value("${agents.service-secret}") String serviceSecret
    ) {
        this.webClient = builder
                .baseUrl(agentsBaseUrl)
                .defaultHeader("X-Service-Secret", serviceSecret)
                .build();
    }

    /**
     * Extract structured contract clauses from an uploaded contract document.
     */
    public Mono<Map<String, Object>> extractClauses(
            UUID contractId,
            String documentUrl
    ) {
        return webClient.post()
                .uri("/v1/extract-clauses")
                .bodyValue(Map.of(
                        "contract_id", contractId.toString(),
                        "document_url", documentUrl
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }

    /**
     * Benchmark the current contract pricing against available market data.
     */
    public Mono<Map<String, Object>> benchmarkPricing(
            UUID contractId,
            String vendorName,
            String planTier,
            Integer seatCount,
            String currency
    ) {
        return webClient.post()
                .uri("/v1/benchmark-pricing")
                .bodyValue(Map.of(
                        "contract_id", contractId.toString(),
                        "vendor_name", vendorName,
                        "plan_tier", planTier,
                        "seat_count", seatCount,
                        "currency", currency
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }

    /**
     * Analyze a contract's upcoming renewal.
     *
     * The agents service returns the AI analysis/recommendation,
     * but apps/api persists it as a Renewal entity.
     */
    public Mono<Map<String, Object>> analyzeRenewal(
            UUID renewalId,
            UUID contractId
    ) {
        return webClient.post()
                .uri("/v1/analyze-renewal")
                .bodyValue(Map.of(
                        "renewal_id", renewalId.toString(),
                        "contract_id", contractId.toString()
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }

    /**
     * Generate the recommended renewal strategy and draft email.
     *
     * Expected response includes fields such as:
     *
     * recommended_action
     * estimated_annual_savings
     * confidence_score
     * reasoning
     * draft_email_subject
     * draft_email_body
     * sources
     * model
     */
    public Mono<Map<String, Object>> generateRecommendation(
            UUID renewalId,
            UUID contractId
    ) {
        return webClient.post()
                .uri("/v1/generate-recommendation")
                .bodyValue(Map.of(
                        "renewal_id", renewalId.toString(),
                        "contract_id", contractId.toString()
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }

    /**
     * Convenience method that runs the complete AI renewal analysis pipeline.
     *
     * apps/api decides when this is called and persists the returned result.
     */
    public Mono<Map<String, Object>> processRenewal(
            UUID renewalId,
            UUID contractId
    ) {
        return webClient.post()
                .uri("/v1/process-renewal")
                .bodyValue(Map.of(
                        "renewal_id", renewalId.toString(),
                        "contract_id", contractId.toString()
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }
}
