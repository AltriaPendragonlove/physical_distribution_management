package com.yixin.logistics.security;

import com.yixin.logistics.entity.User;
import com.yixin.logistics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // 加密后的密码
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().replace("ROLE_", ""))
                        .toArray(String[]::new))
                .build();
    }
}
