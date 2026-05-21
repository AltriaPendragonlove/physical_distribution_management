package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "operation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationLog {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String action; // 比如：LOGIN, UPDATE_PROFILE

    @Column(length = 255)
    private String detail; // 具体描述

    private LocalDateTime createdAt;
}

