package com.cuoco.controller;

import com.cuoco.config.security.utils.JwtUtil;
import com.cuoco.controller.dto.AuthRequest;
import com.cuoco.controller.dto.AuthResponse;
import com.cuoco.service.UserService;
import com.cuoco.service.impl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        userService.create(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }
}
