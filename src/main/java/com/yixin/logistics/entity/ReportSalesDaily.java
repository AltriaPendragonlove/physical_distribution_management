package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reportsalesdaily")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportSalesDaily {

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "order_count", nullable = false)
    private Integer orderCount;

    @Column(length = 50)
    private String region;
}
