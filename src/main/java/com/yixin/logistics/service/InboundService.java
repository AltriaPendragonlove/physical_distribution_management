package com.yixin.logistics.service;

import com.yixin.logistics.entity.InboundOrder;
import com.yixin.logistics.entity.InboundOrderItem;
import com.yixin.logistics.entity.Inventory;
import com.yixin.logistics.repository.InboundOrderRepository;
import com.yixin.logistics.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class InboundService {

    private final InboundOrderRepository inboundOrderRepository;
    private final InventoryRepository inventoryRepository;

    // 创建入库单
    public InboundOrder createInboundOrder(InboundOrder order) {
        order.setCreatedAt(LocalDateTime.now());
        return inboundOrderRepository.save(order);
    }

    // 完成入库（写入库存）
    public void completeInbound(InboundOrder order) {

        for (InboundOrderItem item : order.getItems()) {

            // 查找是否已有库存记录（SKU + 仓库 + 批次）
            Optional<Inventory> optional = inventoryRepository
                    .findBySkuAndWarehouseIdAndBatchNo(
                            item.getSku(),
                            "DEFAULT",   // 你可以改成真实仓库ID
                            item.getBatchNo()
                    );

            Inventory inventory;

            if (optional.isPresent()) {
                // 已有库存 → 数量累加
                inventory = optional.get();
                inventory.setQty(inventory.getQty() + item.getQty());
            } else {
                // 没有库存 → 新建库存记录
                inventory = new Inventory();
                inventory.setSku(item.getSku());
                inventory.setWarehouseId("DEFAULT");
                inventory.setBatchNo(item.getBatchNo());
                inventory.setQty(item.getQty());
                inventory.setLockedQty(0);
                inventory.setExpireDate(item.getExpireDate());
            }

            inventory.setUpdatedAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }
    }

    // 查询入库单
    public InboundOrder getInboundOrder(String id) {
        return inboundOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("入库单不存在"));
    }

    // 分页查询
    public Page<InboundOrder> getInboundOrders(Pageable pageable) {
        return inboundOrderRepository.findAll(pageable);
    }
}
