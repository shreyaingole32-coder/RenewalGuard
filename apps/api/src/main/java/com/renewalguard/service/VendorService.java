package com.renewalguard.service;

import com.renewalguard.domain.entity.Vendor;
import com.renewalguard.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public List<Vendor> list() {
        return vendorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vendor getById(UUID id) {

        return vendorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vendor not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public Vendor findByName(String name) {

        return vendorRepository
                .findByNameIgnoreCase(name)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vendor not found: " + name
                        )
                );
    }
}
