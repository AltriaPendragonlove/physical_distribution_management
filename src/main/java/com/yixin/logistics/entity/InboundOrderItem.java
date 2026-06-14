package com.yixin.logistics.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inboundorderitem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inbound_id", length = 32, nullable = false)
    private String inboundId;

    @Column(length = 32, nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "batch_no", length = 50)
    private String batchNo;

    @Column(name = "expire_date")
    private LocalDate expireDate;

}

