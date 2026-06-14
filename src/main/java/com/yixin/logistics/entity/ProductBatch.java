package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "productbatch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String sku;

    @Column(name = "batch_no", length = 50, nullable = false)
    private String batchNo;

    @Column(name = "produce_date")
    private LocalDate produceDate;

    @Column(name = "expire_date")
    private LocalDate expireDate;
}
