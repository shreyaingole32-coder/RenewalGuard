package com.renewalguard.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pricing_benchmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingBenchmark {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "plan_tier", length = 128)
    private String planTier;

    @Column(name = "seat_count_min")
    private Integer seatCountMin;

    @Column(name = "seat_count_max")
    private Integer seatCountMax;

    @Column(name = "median_annual_price_per_seat", precision = 14, scale = 2)
    private BigDecimal medianAnnualPricePerSeat;

    @Column(name = "low_annual_price_per_seat", precision = 14, scale = 2)
    private BigDecimal lowAnnualPricePerSeat;

    @Column(name = "high_annual_price_per_seat", precision = 14, scale = 2)
    private BigDecimal highAnnualPricePerSeat;

    @Column(name = "data_source", nullable = false, length = 255)
    private String dataSource;

    @Column(name = "sample_size")
    private Integer sampleSize;

    @Column(name = "collected_at", nullable = false)
    private Instant collectedAt;
}
