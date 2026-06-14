package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "customerordertrack")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", length = 32, nullable = false)
    private String orderId;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
