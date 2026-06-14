package com.yixin.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inboundorder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundOrder {

    @Id
    @Column(length = 32)
    private String id;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "inboundOrder", cascade = CascadeType.ALL)
    private List<InboundOrderItem> items = new ArrayList<>();
}
