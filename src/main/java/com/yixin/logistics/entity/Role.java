package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;
}

