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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 自增主键

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String action;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}


