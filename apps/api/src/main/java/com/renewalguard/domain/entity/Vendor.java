package com.renewalguard.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(length = 128)
    private String category;

    @Column(name = "website_url", length = 512)
    private String websiteUrl;

    @Column(name = "logo_url", length = 512)
    private String logoUrl;
}
