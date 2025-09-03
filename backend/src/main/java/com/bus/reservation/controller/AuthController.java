package com.bus.reservation.controller;

import com.bus.reservation.dto.LoginRequest;
import com.bus.reservation.dto.LoginResponse;
import com.bus.reservation.dto.RegisterRequest;
import com.bus.reservation.dto.UserResponse;
import com.bus.reservation.entity.User;
import com.bus.reservation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest req) {
        if (userService.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(req.getPassword()); // Plaintext for now (no JWT)
        user.setRole("CUSTOMER");
        user = userService.save(user);

        UserResponse res = toUserResponse(user);
        return ResponseEntity.created(URI.create("/api/v1/users/" + res.getId())).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        User user = userService.findByEmail(req.getEmail()).orElse(null);
        if (user == null || user.getPassword() == null || !user.getPassword().equals(req.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        LoginResponse lr = new LoginResponse();
        lr.setUser(toUserResponse(user));
        lr.setMessage("OK");
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
