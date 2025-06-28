package com.enterprise.ecm.cases;

import com.enterprise.ecm.shared.tenant.TenantContext;
import com.enterprise.ecm.shared.logging.LoggingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CaseService {
    
    private final CaseRepository caseRepository;
    private final LoggingService loggingService;
    
    public Case createCase(Case caseEntity) {
        loggingService.logInfo("Creating case", caseEntity);
        if (caseEntity.getCaseNumber() == null || caseEntity.getCaseNumber().trim().isEmpty()) {
            caseEntity.setCaseNumber(generateCaseNumber());
        }
        return caseRepository.save(caseEntity);
    }
    
    public Optional<Case> getCaseById(Long id) {
        loggingService.logDebug("Fetching case by id: {}", id);
        return caseRepository.findById(id);
    }
    
    public Optional<Case> getCaseByNumber(String caseNumber) {
        return caseRepository.findByCurrentTenantAndCaseNumber(caseNumber);
    }
    
    public List<Case> getAllCases() {
        return caseRepository.findAllByCurrentTenant();
    }
    
    public Page<Case> getAllCases(Pageable pageable) {
        return caseRepository.findAllByCurrentTenant(pageable);
    }
    
    public List<Case> getCasesByStatus(CaseStatus status) {
        return caseRepository.findByCurrentTenantAndStatus(status);
    }
    
    public List<Case> getCasesByPriority(CasePriority priority) {
        return caseRepository.findByCurrentTenantAndPriority(priority);
    }
    
    public List<Case> getCasesByAssignee(String assignedTo) {
        return caseRepository.findByCurrentTenantAndAssignedTo(assignedTo);
    }
    
    public List<Case> getOverdueCases() {
        return caseRepository.findByCurrentTenantAndDueDateBefore(LocalDateTime.now());
    }
    
    public List<Case> searchCases(String keyword) {
        return caseRepository.findByCurrentTenantAndKeyword(keyword);
    }
    
    public Case updateCase(Long id, Case updatedCase) {
        return caseRepository.findById(id)
                .map(existingCase -> {
                    existingCase.setTitle(updatedCase.getTitle());
                    existingCase.setDescription(updatedCase.getDescription());
                    existingCase.setStatus(updatedCase.getStatus());
                    existingCase.setPriority(updatedCase.getPriority());
                    existingCase.setCategory(updatedCase.getCategory());
                    existingCase.setAssignedTo(updatedCase.getAssignedTo());
                    existingCase.setDueDate(updatedCase.getDueDate());
                    return caseRepository.save(existingCase);
                })
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));
    }
    
    public Case updateCaseStatus(Long id, CaseStatus status) {
        return caseRepository.findById(id)
                .map(caseEntity -> {
                    caseEntity.setStatus(status);
                    return caseRepository.save(caseEntity);
                })
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));
    }
    
    public Case assignCase(Long id, String assignedTo) {
        return caseRepository.findById(id)
                .map(caseEntity -> {
                    caseEntity.setAssignedTo(assignedTo);
                    return caseRepository.save(caseEntity);
                })
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));
    }
    
    public void deleteCase(Long id) {
        loggingService.logWarn("Deleting case: {}", id);
        caseRepository.deleteById(id);
    }
    
    public long getCaseCount() {
        return caseRepository.countByCurrentTenant();
    }
    
    public long getCaseCountByStatus(CaseStatus status) {
        return caseRepository.countByCurrentTenantAndStatus(status);
    }
    
    private String generateCaseNumber() {
        String tenantId = TenantContext.getCurrentTenant();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8);
        return String.format("CASE-%s-%s-%s", tenantId.toUpperCase(), timestamp, random);
    }
} 