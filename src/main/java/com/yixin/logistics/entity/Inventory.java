package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String sku;

    @Column(name = "warehouse_id", length = 32, nullable = false)
    private String warehouseId;

    @Column(name = "batch_no", length = 50, nullable = false)
    private String batchNo;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "locked_qty", nullable = false)
    private Integer lockedQty;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

