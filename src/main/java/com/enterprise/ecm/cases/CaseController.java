package com.enterprise.ecm.cases;

import com.enterprise.ecm.cases.dto.CaseDto;
import com.enterprise.ecm.cases.dto.CreateCaseRequest;
import com.enterprise.ecm.cases.dto.UpdateCaseRequest;
import com.enterprise.ecm.cases.mapper.CaseMapper;
import com.enterprise.ecm.shared.logging.LoggingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cases")
public class CaseController {
    
    private final CaseService caseService;
    private final CaseMapper caseMapper;
    private final LoggingService loggingService;
    
    @PostMapping
    public ResponseEntity<CaseDto> createCase(@Valid @RequestBody CreateCaseRequest request) {
        loggingService.logInfo("Received request to create case", request);
        Case caseEntity = caseMapper.toEntity(request);
        Case createdCase = caseService.createCase(caseEntity);
        CaseDto response = caseMapper.toDto(createdCase);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CaseDto> getCaseById(@PathVariable Long id) {
        loggingService.logDebug("Received request to get case by id: {}", id);
        Optional<Case> caseEntity = caseService.getCaseById(id);
        return caseEntity.map(entity -> ResponseEntity.ok(caseMapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{caseNumber}")
    public ResponseEntity<CaseDto> getCaseByNumber(@PathVariable String caseNumber) {
        Optional<Case> caseEntity = caseService.getCaseByNumber(caseNumber);
        return caseEntity.map(entity -> ResponseEntity.ok(caseMapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<Page<CaseDto>> getAllCases(Pageable pageable) {
        Page<Case> cases = caseService.getAllCases(pageable);
        Page<CaseDto> response = cases.map(caseMapper::toDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<CaseDto>> getAllCasesList() {
        List<Case> cases = caseService.getAllCases();
        List<CaseDto> response = caseMapper.toDtoList(cases);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CaseDto>> getCasesByStatus(@PathVariable CaseStatus status) {
        List<Case> cases = caseService.getCasesByStatus(status);
        List<CaseDto> response = caseMapper.toDtoList(cases);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<CaseDto>> getCasesByPriority(@PathVariable CasePriority priority) {
        List<Case> cases = caseService.getCasesByPriority(priority);
        List<CaseDto> response = caseMapper.toDtoList(cases);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/assignee/{assignedTo}")
    public ResponseEntity<List<CaseDto>> getCasesByAssignee(@PathVariable String assignedTo) {
        List<Case> cases = caseService.getCasesByAssignee(assignedTo);
        List<CaseDto> response = caseMapper.toDtoList(cases);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<CaseDto>> getOverdueCases() {
        List<Case> cases = caseService.getOverdueCases();
        List<CaseDto> response = caseMapper.toDtoList(cases);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CaseDto>> searchCases(@RequestParam String keyword) {
        List<Case> cases = caseService.searchCases(keyword);
        List<CaseDto> response = caseMapper.toDtoList(cases);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CaseDto> updateCase(@PathVariable Long id, @Valid @RequestBody UpdateCaseRequest request) {
        try {
            Optional<Case> existingCase = caseService.getCaseById(id);
            if (existingCase.isPresent()) {
                Case caseEntity = existingCase.get();
                caseMapper.updateEntityFromRequestIgnoreNull(request, caseEntity);
                Case updatedCase = caseService.updateCase(id, caseEntity);
                CaseDto response = caseMapper.toDto(updatedCase);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<CaseDto> updateCaseStatus(@PathVariable Long id, @RequestParam CaseStatus status) {
        try {
            Case caseEntity = caseService.updateCaseStatus(id, status);
            CaseDto response = caseMapper.toDto(caseEntity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/assign")
    public ResponseEntity<CaseDto> assignCase(@PathVariable Long id, @RequestParam String assignedTo) {
        try {
            Case caseEntity = caseService.assignCase(id, assignedTo);
            CaseDto response = caseMapper.toDto(caseEntity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        loggingService.logWarn("Received request to delete case: {}", id);
        caseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getCaseCount() {
        long count = caseService.getCaseCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/count/{status}")
    public ResponseEntity<Long> getCaseCountByStatus(@PathVariable CaseStatus status) {
        long count = caseService.getCaseCountByStatus(status);
        return ResponseEntity.ok(count);
    }
} 