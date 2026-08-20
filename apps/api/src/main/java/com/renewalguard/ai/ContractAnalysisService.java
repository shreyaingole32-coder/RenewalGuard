
package com.renewalguard.ai;

import com.renewalguard.domain.entity.Contract;
import com.renewalguard.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractAnalysisService {

    private final ContractRepository contractRepository;
    private final AgentsClient agentsClient;

    @Transactional(readOnly = true)
    public Map<String, Object> analyze(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Contract not found: " + contractId));

        return agentsClient
                .analyzeContract(contract.getId())
                .blockOptional()
                .orElseThrow(() ->
                        new IllegalStateException("Agents service returned no analysis"));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> extractClauses(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Contract not found: " + contractId));

        if (contract.getDocumentUrl() == null ||
                contract.getDocumentUrl().isBlank()) {
            throw new IllegalStateException(
                    "Contract does not have a document URL: " + contractId
            );
        }

        return agentsClient
                .extractClauses(contract.getId(), contract.getDocumentUrl())
                .blockOptional()
                .orElseThrow(() ->
                        new IllegalStateException("Agents service returned no clauses"));
    }
}
