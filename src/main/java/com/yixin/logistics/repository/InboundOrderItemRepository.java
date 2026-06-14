package com.yixin.logistics.repository;

import com.yixin.logistics.entity.InboundOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundOrderItemRepository extends JpaRepository<InboundOrderItem, Long> {
}
