package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(length = 32)
    private String sku;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100)
    private String spec;

    @Column(length = 20, nullable = false)
    private String unit;

    @Column(length = 50)
    private String barcode;
}

