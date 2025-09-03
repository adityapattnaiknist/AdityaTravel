package com.bus.reservation.controller;

import com.bus.reservation.dto.LoginRequest;
import com.bus.reservation.dto.LoginResponse;
import com.bus.reservation.dto.RegisterRequest;
import com.bus.reservation.dto.UserResponse;
import com.bus.reservation.entity.User;
import com.bus.reservation.service.JwtService;
import com.bus.reservation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest req) {
        if (userService.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole("CUSTOMER");
        user = userService.save(user);

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        String token = jwtService.generateToken(userDetails);

        LoginResponse lr = new LoginResponse();
        lr.setUser(toUserResponse(user));
        lr.setToken(token);
        return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId())).body(lr);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        User user = userService.findByEmail(req.getEmail()).orElseThrow();
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        String token = jwtService.generateToken(userDetails);

        LoginResponse lr = new LoginResponse();
        lr.setUser(toUserResponse(user));
        lr.setToken(token);
        return ResponseEntity.ok(lr);
    }

    private static UserResponse toUserResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setName(u.getName());
        r.setEmail(u.getEmail());
        r.setPhone(u.getPhone());
        r.setRole(u.getRole());
        return r;
    }
}
