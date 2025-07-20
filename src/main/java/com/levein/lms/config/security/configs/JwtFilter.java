package com.levein.lms.config.security.configs;

import com.levein.lms.constant.MessageConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {

            log.info("JwtFilter.doFilterInternal() invoked");
            String path = request.getServletPath();
            if (path.startsWith("/api/v1/auth") ||
                    path.startsWith("/swagger-ui") ||
                    path.startsWith("/v3/api-docs") ||
                    path.equals("/swagger-ui.html")) {
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                // Token missing
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MessageConstants.Common.APPLICATION_JSON);
                response.getWriter().write("""
                {
                    "status": 401,
                    "message": "Unauthorized - Token is missing",
                    "timestamp": "%s"
                }
                """.formatted(LocalDateTime.now()));

                return;
            }



            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "status": 401,
                    "message": "Unauthorized - Invalid credentials or token",
                    "timestamp": "%s"
                }
                """.formatted(LocalDateTime.now()));
        }
    }
}



