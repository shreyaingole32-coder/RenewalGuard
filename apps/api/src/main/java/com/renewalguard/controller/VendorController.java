
package com.renewalguard.controller;

import com.renewalguard.domain.entity.Vendor;
import com.renewalguard.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorRepository vendorRepository;

    @GetMapping
    public List<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{vendorId}")
    public Vendor get(
            @PathVariable UUID vendorId
    ) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vendor not found: " + vendorId
                        ));
    }

    @PostMapping
    public Vendor create(
            @RequestBody Vendor vendor
    ) {
        return vendorRepository.save(vendor);
    }
}
