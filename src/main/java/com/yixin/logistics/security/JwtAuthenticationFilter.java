package com.yixin.logistics.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //  放行登录和注册接口
        String path = request.getServletPath();
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 1. 从请求头获取 Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 2. 提取 token
            String token = authHeader.substring(7);

            // 3. 验证 token
            if (jwtTokenProvider.validateToken(token)) {

                // 4. 从 token 获取用户名
                String username = jwtTokenProvider.getUsername(token);

                // 5. 从数据库加载用户
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 6. 创建认证对象
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 7. 放入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }
}
