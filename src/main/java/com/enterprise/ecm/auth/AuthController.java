package com.enterprise.ecm.auth;

import com.enterprise.ecm.auth.dto.LoginResponse;
import com.enterprise.ecm.security.JwtTokenProvider;
import com.enterprise.ecm.users.User;
import com.enterprise.ecm.users.UserService;
import com.enterprise.ecm.users.dto.UserDto;
import com.enterprise.ecm.users.mapper.UserMapper;
import com.enterprise.ecm.shared.logging.LoggingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;
    private final LoggingService loggingService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        loggingService.logInfo("Login attempt for user: {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        LoginResponse response = new LoginResponse(jwt, "Bearer", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);
        UserDto response = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(userMapper.toDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth controller is working!");
    }
    
    @GetMapping("/test-password")
    public ResponseEntity<Map<String, Object>> testPassword() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Test password encoding
            String rawPassword = "password123";
            String encodedPassword = "$2b$10$b5UQ0eZmOza.7p2MIdaYQuFV.5cT7Ty8q8hjDLM6GM6cec.DzBlYW";
            
            // Test user loading
            Optional<User> user = userService.getUserByUsername("admin");
            
            result.put("rawPassword", rawPassword);
            result.put("encodedPassword", encodedPassword);
            result.put("userFound", user.isPresent());
            
            if (user.isPresent()) {
                User userEntity = user.get();
                result.put("username", userEntity.getUsername());
                result.put("userPassword", userEntity.getPassword());
                result.put("userRoles", userEntity.getRoles());
                result.put("userActive", userEntity.isActive());
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
} 