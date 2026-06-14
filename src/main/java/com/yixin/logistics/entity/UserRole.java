package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userrole")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 建议加主键

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;
}

