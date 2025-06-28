package com.enterprise.ecm.users;

import com.enterprise.ecm.shared.tenant.TenantContext;
import com.enterprise.ecm.users.dto.CreateUserRequest;
import com.enterprise.ecm.users.dto.UpdateUserRequest;
import com.enterprise.ecm.users.dto.UserDto;
import com.enterprise.ecm.users.mapper.UserMapper;
import com.enterprise.ecm.shared.logging.LoggingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final LoggingService loggingService;
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        loggingService.logInfo("Received request to create user: {}", request.getUsername());
        User user = userService.createUser(request);
        UserDto response = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        Page<UserDto> response = users.map(userMapper::toDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsersList() {
        List<User> users = userService.getAllUsers();
        List<UserDto> response = userMapper.toDtoList(users);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        loggingService.logDebug("Received request to get user by id: {}", id);
        Optional<User> user = userService.getUserById(id);
        return user.map(entity -> ResponseEntity.ok(userMapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(entity -> ResponseEntity.ok(userMapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(entity -> ResponseEntity.ok(userMapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<List<UserDto>> getUsersByDepartment(@PathVariable String department) {
        List<User> users = userService.getUsersByDepartment(department);
        List<UserDto> response = userMapper.toDtoList(users);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        List<UserDto> response = userMapper.toDtoList(users);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        List<User> users = userService.getActiveUsers();
        List<UserDto> response = userMapper.toDtoList(users);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                userMapper.updateEntityFromRequestIgnoreNull(request, user);
                User updatedUser = userService.updateUser(id, user);
                UserDto response = userMapper.toDto(updatedUser);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable Long id, @RequestParam String newPassword) {
        try {
            User user = userService.updatePassword(id, newPassword);
            UserDto response = userMapper.toDto(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getUserCount() {
        long count = userService.getUserCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/active-count")
    public ResponseEntity<Long> getActiveUserCount() {
        long count = userService.getActiveUserCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/debug/users")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Map<String, Object>> debugUsers() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Set tenant context for this request
            TenantContext.setCurrentTenant("default");
            
            List<User> allUsers = userService.getAllUsers();
            result.put("totalUsers", allUsers.size());
            
            List<Map<String, Object>> userDetails = new ArrayList<>();
            for (User user : allUsers) {
                Map<String, Object> userDetail = new HashMap<>();
                userDetail.put("id", user.getId());
                userDetail.put("username", user.getUsername());
                userDetail.put("email", user.getEmail());
                userDetail.put("tenantId", user.getTenantId());
                userDetail.put("active", user.isActive());
                userDetail.put("roles", user.getRoles());
                
                userDetails.add(userDetail);
            }
            result.put("users", userDetails);
            
            // Clear tenant context
            TenantContext.clear();
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
} 