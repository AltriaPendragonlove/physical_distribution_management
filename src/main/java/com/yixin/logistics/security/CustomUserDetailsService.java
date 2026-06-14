package com.yixin.logistics.security;

import com.yixin.logistics.entity.UserAccount;
import com.yixin.logistics.entity.UserRole;
import com.yixin.logistics.repository.RoleRepository;
import com.yixin.logistics.repository.UserRepository;
import com.yixin.logistics.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.yixin.logistics.entity.Role;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        // 查用户角色
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());

        String[] roles = userRoles.stream()
                .map(ur -> roleRepository.findById(ur.getRoleId())
                        .map(Role::getName)
                        .orElse("ROLE_USER"))
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(roles) // 直接使用 ROLE_ADMIN
                .build();
    }
}

