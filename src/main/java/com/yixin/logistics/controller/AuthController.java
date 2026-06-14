package com.yixin.logistics.controller;

import com.yixin.logistics.entity.UserAccount;
import com.yixin.logistics.entity.UserRole;
import com.yixin.logistics.repository.RoleRepository;
import com.yixin.logistics.repository.UserRepository;
import com.yixin.logistics.repository.UserRoleRepository;
import com.yixin.logistics.security.JwtTokenProvider;
import com.yixin.logistics.service.AuthService;
import com.yixin.logistics.web.LoginRequest;
import com.yixin.logistics.web.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yixin.logistics.entity.Role;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // 注册
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("注册成功");
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("未登录");
        }

        String token = authHeader.substring(7);
        String username = jwtTokenProvider.getUsername(token);

        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());

        List<String> roles = userRoles.stream()
                .map(ur -> roleRepository.findById(ur.getRoleId())
                        .map(Role::getName)
                        .orElse("ROLE_USER"))
                .toList();


        return ResponseEntity.ok(new HashMap<>() {{
            put("id", user.getId());
            put("username", user.getUsername());
            put("roles", roles);
        }});
    }


}
