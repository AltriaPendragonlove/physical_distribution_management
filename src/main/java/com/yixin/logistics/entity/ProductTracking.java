package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "producttracking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTracking {

    @Id
    @Column(name = "tracking_id", length = 32)
    private String trackingId;

    @Column(length = 32, nullable = false)
    private String sku;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(length = 100)
    private String location;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}

