package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rolepermission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 数据库没有主键，加一个

    @Column(name = "role_id", length = 32, nullable = false)
    private String roleId;

    @Column(name = "permission_id", length = 32, nullable = false)
    private String permissionId;
}

