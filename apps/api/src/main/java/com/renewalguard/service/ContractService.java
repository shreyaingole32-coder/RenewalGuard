
package com.renewalguard.service;

import com.renewalguard.domain.entity.Contract;
import com.renewalguard.domain.entity.User;
import com.renewalguard.dto.ContractResponse;
import com.renewalguard.dto.CreateContractRequest;
import com.renewalguard.repository.ContractRepository;
import com.renewalguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ContractResponse> listForOrganization(
            UUID organizationId) {

        return contractRepository
                .findByOrganizationId(organizationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Contract getById(UUID contractId) {

        return contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Contract not found: " + contractId
                        )
                );
    }

    @Transactional
    public Contract create(
            CreateContractRequest request) {

        User owner = userRepository
                .findById(request.ownerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Owner not found: " +
                                        request.ownerId()
                        )
                );

        Contract contract = Contract.builder()
                .owner(owner)
                .vendorName(request.vendorName())
                .contractName(request.contractName())
                .contractType(request.contractType())
                .annualValue(request.annualValue())
                .currency(
                        request.currency() == null
                                ? "USD"
                                : request.currency()
                )
                .startDate(request.startDate())
                .endDate(request.endDate())
                .autoRenews(request.autoRenews())
                .cancellationNoticeDays(
                        request.cancellationNoticeDays()
                )
                .documentUrl(request.documentUrl())
                .build();

        return contractRepository.save(contract);
    }

    private ContractResponse toResponse(Contract contract) {

        long daysUntilRenewal =
                contract.getEndDate() == null
                        ? -1
                        : ChronoUnit.DAYS.between(
                                LocalDate.now(),
                                contract.getEndDate()
                        );

        return new ContractResponse(
                contract.getId(),
                contract.getVendorName(),
                contract.getContractName(),
                contract.getContractType(),
                contract.getAnnualValue(),
                contract.getCurrency(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.isAutoRenews(),
                contract.getCancellationNoticeDays(),
                null,
                contract.getOwner().getId(),
                contract.getOwner().getEmail(),
                contract.getDocumentUrl(),
                daysUntilRenewal
        );
    }
}
