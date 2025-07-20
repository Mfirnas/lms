package com.levein.lms.config.security.controller;


import com.levein.lms.config.security.service.AuthService;
import com.levein.lms.dto.request.AuthRequest;
import com.levein.lms.dto.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", 401,
                    "error", "Unauthorized",
                    "message", e.getMessage()
            ));
        }
    }
//    @PostMapping("/login")
//    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//            String token = jwtUtil.generateToken(userDetails.getUsername());
//
//            return ResponseEntity.ok(new AuthResponse(token));
//        } catch (BadCredentialsException exception) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
//                    "status", 401,
//                    "error", "Unauthorized",
//                    "message", "Invalid username or password"
//            ));
//        }

}
