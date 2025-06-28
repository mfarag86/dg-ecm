package com.enterprise.ecm.users;

import com.enterprise.ecm.shared.tenant.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId")
    List<User> findAllByTenant(@Param("tenantId") String tenantId);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId")
    Page<User> findAllByTenant(@Param("tenantId") String tenantId, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.username = :username")
    Optional<User> findByTenantAndUsername(@Param("tenantId") String tenantId, @Param("username") String username);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.email = :email")
    Optional<User> findByTenantAndEmail(@Param("tenantId") String tenantId, @Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.department = :department")
    List<User> findByTenantAndDepartment(@Param("tenantId") String tenantId, @Param("department") String department);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.active = :active")
    List<User> findByTenantAndActive(@Param("tenantId") String tenantId, @Param("active") boolean active);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND :role MEMBER OF u.roles")
    List<User> findByTenantAndRole(@Param("tenantId") String tenantId, @Param("role") String role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.tenantId = :tenantId")
    long countByTenant(@Param("tenantId") String tenantId);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.tenantId = :tenantId AND u.active = :active")
    long countByTenantAndActive(@Param("tenantId") String tenantId, @Param("active") boolean active);
    
    // Default methods that use current tenant
    default List<User> findAllByCurrentTenant() {
        return findAllByTenant(TenantContext.getCurrentTenant());
    }
    
    default Page<User> findAllByCurrentTenant(Pageable pageable) {
        return findAllByTenant(TenantContext.getCurrentTenant(), pageable);
    }
    
    default Optional<User> findByCurrentTenantAndUsername(String username) {
        return findByTenantAndUsername(TenantContext.getCurrentTenant(), username);
    }
    
    default Optional<User> findByCurrentTenantAndEmail(String email) {
        return findByTenantAndEmail(TenantContext.getCurrentTenant(), email);
    }
    
    default List<User> findByCurrentTenantAndDepartment(String department) {
        return findByTenantAndDepartment(TenantContext.getCurrentTenant(), department);
    }
    
    default List<User> findByCurrentTenantAndActive(boolean active) {
        return findByTenantAndActive(TenantContext.getCurrentTenant(), active);
    }
    
    default List<User> findByCurrentTenantAndRole(String role) {
        return findByTenantAndRole(TenantContext.getCurrentTenant(), role);
    }
    
    default long countByCurrentTenant() {
        return countByTenant(TenantContext.getCurrentTenant());
    }
    
    default long countByCurrentTenantAndActive(boolean active) {
        return countByTenantAndActive(TenantContext.getCurrentTenant(), active);
    }
} 