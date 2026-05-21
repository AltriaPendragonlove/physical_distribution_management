package com.yixin.logistics.repository;

import com.yixin.logistics.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    List<OperationLog> findByUserId(String userId);
}
