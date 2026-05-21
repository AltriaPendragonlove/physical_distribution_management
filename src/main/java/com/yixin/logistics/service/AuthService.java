package com.yixin.logistics.service;

import com.yixin.logistics.entity.OperationLog;
import com.yixin.logistics.entity.Role;
import com.yixin.logistics.entity.User;
import com.yixin.logistics.repository.OperationLogRepository;
import com.yixin.logistics.repository.RoleRepository;
import com.yixin.logistics.repository.UserRepository;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
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

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被使用");
        }

        // 默认角色：ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                ));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        // 记录日志
        logRepository.save(OperationLog.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .action("REGISTER")
                .detail("用户注册")
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

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 生成 JWT
        String token = jwtTokenProvider.createToken(user.getUsername());

        // 记录日志
        logRepository.save(OperationLog.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .action("LOGIN")
                .detail("用户登录")
                .createdAt(LocalDateTime.now())
                .build());

        return token;
    }
}
