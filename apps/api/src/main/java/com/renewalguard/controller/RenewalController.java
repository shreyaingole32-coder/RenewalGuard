
package com.renewalguard.controller;

import com.renewalguard.dto.GenerateRenewalRequest;
import com.renewalguard.dto.RenewalResponse;
import com.renewalguard.service.RenewalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/renewals")
@RequiredArgsConstructor
public class RenewalController {

    private final RenewalService renewalService;

    @GetMapping
    public List<RenewalResponse> list() {
        return renewalService.list();
    }

    @GetMapping("/{renewalId}")
    public RenewalResponse get(
            @PathVariable UUID renewalId
    ) {
        return renewalService.get(renewalId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN')")
    public RenewalResponse create(
            @Valid @RequestBody GenerateRenewalRequest request
    ) {
        return renewalService.create(request);
    }

    @PostMapping("/{renewalId}/analyze")
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN')")
    public RenewalResponse analyze(
            @PathVariable UUID renewalId
    ) {
        return renewalService.analyze(renewalId);
    }
}
