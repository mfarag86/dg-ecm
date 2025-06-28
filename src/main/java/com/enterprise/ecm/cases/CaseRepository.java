package com.enterprise.ecm.cases;

import com.enterprise.ecm.shared.tenant.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId")
    List<Case> findAllByTenant(@Param("tenantId") String tenantId);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId")
    Page<Case> findAllByTenant(@Param("tenantId") String tenantId, Pageable pageable);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId AND c.caseNumber = :caseNumber")
    Optional<Case> findByTenantAndCaseNumber(@Param("tenantId") String tenantId, @Param("caseNumber") String caseNumber);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId AND c.status = :status")
    List<Case> findByTenantAndStatus(@Param("tenantId") String tenantId, @Param("status") CaseStatus status);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId AND c.priority = :priority")
    List<Case> findByTenantAndPriority(@Param("tenantId") String tenantId, @Param("priority") CasePriority priority);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId AND c.assignedTo = :assignedTo")
    List<Case> findByTenantAndAssignedTo(@Param("tenantId") String tenantId, @Param("assignedTo") String assignedTo);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId AND c.dueDate <= :dueDate")
    List<Case> findByTenantAndDueDateBefore(@Param("tenantId") String tenantId, @Param("dueDate") LocalDateTime dueDate);
    
    @Query("SELECT c FROM Case c WHERE c.tenantId = :tenantId AND c.title LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Case> findByTenantAndKeyword(@Param("tenantId") String tenantId, @Param("keyword") String keyword);
    
    @Query("SELECT COUNT(c) FROM Case c WHERE c.tenantId = :tenantId AND c.status = :status")
    long countByTenantAndStatus(@Param("tenantId") String tenantId, @Param("status") CaseStatus status);
    
    @Query("SELECT COUNT(c) FROM Case c WHERE c.tenantId = :tenantId")
    long countByTenant(@Param("tenantId") String tenantId);
    
    // Default methods that use current tenant
    default List<Case> findAllByCurrentTenant() {
        return findAllByTenant(TenantContext.getCurrentTenant());
    }
    
    default Page<Case> findAllByCurrentTenant(Pageable pageable) {
        return findAllByTenant(TenantContext.getCurrentTenant(), pageable);
    }
    
    default Optional<Case> findByCurrentTenantAndCaseNumber(String caseNumber) {
        return findByTenantAndCaseNumber(TenantContext.getCurrentTenant(), caseNumber);
    }
    
    default List<Case> findByCurrentTenantAndStatus(CaseStatus status) {
        return findByTenantAndStatus(TenantContext.getCurrentTenant(), status);
    }
    
    default List<Case> findByCurrentTenantAndPriority(CasePriority priority) {
        return findByTenantAndPriority(TenantContext.getCurrentTenant(), priority);
    }
    
    default List<Case> findByCurrentTenantAndAssignedTo(String assignedTo) {
        return findByTenantAndAssignedTo(TenantContext.getCurrentTenant(), assignedTo);
    }
    
    default List<Case> findByCurrentTenantAndDueDateBefore(LocalDateTime dueDate) {
        return findByTenantAndDueDateBefore(TenantContext.getCurrentTenant(), dueDate);
    }
    
    default List<Case> findByCurrentTenantAndKeyword(String keyword) {
        return findByTenantAndKeyword(TenantContext.getCurrentTenant(), keyword);
    }
    
    default long countByCurrentTenantAndStatus(CaseStatus status) {
        return countByTenantAndStatus(TenantContext.getCurrentTenant(), status);
    }
    
    default long countByCurrentTenant() {
        return countByTenant(TenantContext.getCurrentTenant());
    }
} 