package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportinventorysnapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportInventorySnapshot {

    @Id
    @Column(name = "snapshot_time")
    private LocalDateTime snapshotTime;

    @Column(length = 32, nullable = false)
    private String sku;

    @Column(name = "warehouse_id", length = 32, nullable = false)
    private String warehouseId;

    @Column(nullable = false)
    private Integer qty;
}

