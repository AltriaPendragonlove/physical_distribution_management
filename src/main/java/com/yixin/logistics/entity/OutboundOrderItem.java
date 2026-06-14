package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "outboundorderitem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboundOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "outbound_id", length = 32, nullable = false)
    private String outboundId;

    @Column(length = 32, nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "batch_no", length = 50)
    private String batchNo;
}

