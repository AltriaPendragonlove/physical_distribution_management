package com.yixin.logistics.repository;

import com.yixin.logistics.entity.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundOrderRepository extends JpaRepository<InboundOrder, String> {
}


