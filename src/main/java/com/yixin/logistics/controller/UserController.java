package com.yixin.logistics.controller;

import com.yixin.logistics.entity.UserAccount;
import com.yixin.logistics.entity.UserRole;
import com.yixin.logistics.repository.RoleRepository;
import com.yixin.logistics.repository.UserRepository;
import com.yixin.logistics.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.yixin.logistics.entity.Role;




@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public UserAccount getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserAccount> user = userRepository.findByUsername(username);
        return user.orElse(null);

    }

    // 获取用户列表（管理员）
    @GetMapping
    public List<Map<String, Object>> listUsers() {

        List<UserAccount> users = userRepository.findAll();

        return users.stream().map(user -> {

            List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
            List<String> roles = userRoles.stream()
                    .map(ur -> roleRepository.findById(ur.getRoleId())
                            .map(Role::getName)
                            .orElse("ROLE_USER"))
                    .toList();

            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            map.put("roles", roles);

            return map;

        }).toList();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<?> assignRole(
            @PathVariable String userId,
            @RequestBody Map<String, String> request
    ) {
        String roleName = request.get("role");

        // 1. 找到角色
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("角色不存在：" + roleName));

        // 先检查是否已经拥有该角色
        boolean exists = userRoleRepository.existsByUserIdAndRoleId(userId, role.getId());
        if (exists) {
            return ResponseEntity.badRequest().body("用户已经拥有该角色");
        }

        // 2. 创建 userrole 关系
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());

        userRoleRepository.save(userRole);

        return ResponseEntity.ok("角色分配成功");
    }

    @Transactional
    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<?> removeRole(
            @PathVariable String userId,
            @PathVariable String roleName
    ) {
        // 1. 找到角色
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("角色不存在：" + roleName));

        // 2. 检查用户是否拥有该角色
        boolean exists = userRoleRepository.existsByUserIdAndRoleId(userId, role.getId());
        if (!exists) {
            return ResponseEntity.badRequest().body("用户没有该角色");
        }

        // 3. 删除关系
        userRoleRepository.deleteByUserIdAndRoleId(userId, role.getId());

        return ResponseEntity.ok("角色移除成功");
    }

    @PatchMapping("/{userId}/disable")
    public ResponseEntity<?> disableUser(@PathVariable String userId) {

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setEnabled(false);
        userRepository.save(user);

        return ResponseEntity.ok("用户已被禁用");
    }

    @PatchMapping("/{userId}/enable")
    public ResponseEntity<?> enableUser(@PathVariable String userId) {

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok("用户已被启用");
    }

    @GetMapping("/page")
    public ResponseEntity<?> getUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserAccount> userPage = userRepository.findAll(pageable);

        return ResponseEntity.ok(userPage);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        // 校验旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("旧密码错误");
        }

        // 更新新密码
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("密码修改成功");
    }

    @PatchMapping("/{userId}/reset-password")
    public ResponseEntity<?> resetPassword(
            @PathVariable String userId,
            @RequestBody Map<String, String> request
    ) {
        String newPassword = request.get("newPassword");

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("密码已被管理员重置");
    }


}
