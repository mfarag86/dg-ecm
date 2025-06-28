package com.enterprise.ecm.users;

import com.enterprise.ecm.auth.RegisterRequest;
import com.enterprise.ecm.users.dto.CreateUserRequest;
import com.enterprise.ecm.shared.tenant.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
@Transactional
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Temporarily ignore tenant context for debugging
        Optional<User> user = userRepository.findByUsernameIgnoreTenant(username);
        
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        User userEntity = user.get();
        
        // The roles should be loaded automatically due to @ElementCollection(fetch = FetchType.EAGER)
        // If roles are still empty, we'll use a default role
        if (userEntity.getRoles().isEmpty()) {
            userEntity.addRole("USER");
        }
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList()))
                .disabled(!userEntity.isActive())
                .build();
    }
    
    public User createUser(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.findByCurrentTenantAndUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.findByCurrentTenantAndEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setDepartment(registerRequest.getDepartment());
        user.setPosition(registerRequest.getPosition());
        user.setActive(true);
        
        // Set default role if none provided
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            user.addRole("USER");
        } else {
            user.setRoles(registerRequest.getRoles());
        }
        
        return userRepository.save(user);
    }
    
    public User createUser(CreateUserRequest request) {
        // Check if username already exists
        if (userRepository.findByCurrentTenantAndUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Check if email already exists
        if (userRepository.findByCurrentTenantAndEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setActive(true);
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            user.addRole("USER");
        } else {
            user.setRoles(request.getRoles());
        }
        return userRepository.save(user);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        // Get current tenant, default to 'default' if not set
        String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant == null) {
            currentTenant = "default";
        }
        return userRepository.findByTenantAndUsername(currentTenant, username);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByCurrentTenantAndEmail(email);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAllByCurrentTenant();
    }
    
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAllByCurrentTenant(pageable);
    }
    
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByCurrentTenantAndDepartment(department);
    }
    
    public List<User> getUsersByRole(String role) {
        return userRepository.findByCurrentTenantAndRole(role);
    }
    
    public List<User> getActiveUsers() {
        return userRepository.findByCurrentTenantAndActive(true);
    }
    
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setDepartment(updatedUser.getDepartment());
                    existingUser.setPosition(updatedUser.getPosition());
                    existingUser.setActive(updatedUser.isActive());
                    existingUser.setRoles(updatedUser.getRoles());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public User updatePassword(Long id, String newPassword) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public User updateLastLogin(String username) {
        return userRepository.findByCurrentTenantAndUsername(username)
                .map(user -> {
                    user.setLastLogin(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public void deactivateUser(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                });
    }
    
    public void activateUser(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> {
                    user.setActive(true);
                    userRepository.save(user);
                });
    }
    
    public long getUserCount() {
        return userRepository.countByCurrentTenant();
    }
    
    public long getActiveUserCount() {
        return userRepository.countByCurrentTenantAndActive(true);
    }
} 