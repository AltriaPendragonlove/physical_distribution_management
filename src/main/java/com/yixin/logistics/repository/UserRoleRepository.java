package com.yixin.logistics.repository;

import com.yixin.logistics.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(String userId);

    boolean existsByUserIdAndRoleId(String userId, Long roleId);

    void deleteByUserIdAndRoleId(String userId, Long roleId);


}
