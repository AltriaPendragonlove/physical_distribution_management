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

        String path = request.getServletPath();

        //  1. 放行 /auth/**（注册、登录）
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        //  2. 放行 OPTIONS 预检请求（否则依然 403）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        //  3. 获取 Authorization 头
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 没有 token → 放行（但后续接口会被 Security 拦住）
            filterChain.doFilter(request, response);
            return;
        }

        //  4. 提取 token
        String token = authHeader.substring(7);

        //  5. 验证 token
        if (!jwtTokenProvider.validateToken(token)) {
            // token 无效 → 放行（但不会设置认证）
            filterChain.doFilter(request, response);
            return;
        }

        //  6. 从 token 获取用户名
        String username = jwtTokenProvider.getUsername(token);

        //  7. 如果用户未认证，则加载用户信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        //  8. 放行
        filterChain.doFilter(request, response);
    }
}
