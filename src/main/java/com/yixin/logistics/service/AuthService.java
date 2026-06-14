package com.yixin.logistics.service;

import com.yixin.logistics.entity.OperationLog;
import com.yixin.logistics.entity.Role;
import com.yixin.logistics.entity.UserAccount;
import com.yixin.logistics.entity.UserRole;
import com.yixin.logistics.repository.OperationLogRepository;
import com.yixin.logistics.repository.RoleRepository;
import com.yixin.logistics.repository.UserRepository;
import com.yixin.logistics.repository.UserRoleRepository;
import com.yixin.logistics.security.JwtTokenProvider;
import com.yixin.logistics.web.LoginRequest;
import com.yixin.logistics.web.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final OperationLogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // 注册
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 默认角色：ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("角色不存在"));

        UserAccount user = UserAccount.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        userRoleRepository.save(
                UserRole.builder()
                        .userId(user.getId())
                        .roleId(userRole.getId())
                        .build()
        );

        logRepository.save(OperationLog.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .action("REGISTER")
                .createdAt(LocalDateTime.now())
                .build());
    }

    // 登录
    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        UserAccount user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!user.isEnabled()) {
            throw new RuntimeException("用户已被禁用");
        }

        String token = jwtTokenProvider.createToken(user.getUsername());

        logRepository.save(OperationLog.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .action("LOGIN")
                .createdAt(LocalDateTime.now())
                .build());

        return token;
    }
}

