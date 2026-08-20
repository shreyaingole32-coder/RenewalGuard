package com.renewalguard.controller;

import com.renewalguard.dto.ContractResponse;
import com.renewalguard.dto.CreateContractRequest;
import com.renewalguard.dto.UpdateContractRequest;
import com.renewalguard.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public List<ContractResponse> list() {
        return contractService.list();
    }

    @GetMapping("/{contractId}")
    public ContractResponse get(
            @PathVariable UUID contractId
    ) {
        return contractService.get(contractId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN')")
    public ContractResponse create(
            @Valid @RequestBody CreateContractRequest request
    ) {
        return contractService.create(request);
    }

    @PutMapping("/{contractId}")
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN')")
    public ContractResponse update(
            @PathVariable UUID contractId,
            @Valid @RequestBody UpdateContractRequest request
    ) {
        return contractService.update(contractId, request);
    }

    @DeleteMapping("/{contractId}")
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN')")
    public void delete(
            @PathVariable UUID contractId
    ) {
        contractService.delete(contractId);
    }
}
