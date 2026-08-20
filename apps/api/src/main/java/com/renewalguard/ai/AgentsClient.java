package com.renewalguard.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

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

    public Mono<Map<String, Object>> analyzeContract(UUID contractId) {
        return webClient.post()
                .uri("/v1/analyze-contract")
                .bodyValue(Map.of(
                        "contract_id", contractId.toString()
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }

    public Mono<Map<String, Object>> benchmarkPricing(
            UUID contractId,
            String vendorName,
            String currency
    ) {
        return webClient.post()
                .uri("/v1/benchmark-pricing")
                .bodyValue(Map.of(
                        "contract_id", contractId.toString(),
                        "vendor_name", vendorName,
                        "currency", currency
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(result -> (Map<String, Object>) result);
    }

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
