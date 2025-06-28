package com.enterprise.ecm.cases.mapper;

import com.enterprise.ecm.cases.Case;
import com.enterprise.ecm.cases.CasePriority;
import com.enterprise.ecm.cases.CaseStatus;
import com.enterprise.ecm.cases.dto.CaseDto;
import com.enterprise.ecm.cases.dto.CreateCaseRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CaseMapperTest {
    
    private final CaseMapper mapper = Mappers.getMapper(CaseMapper.class);
    
    @Test
    void testCreateCaseRequestToEntity() {
        // Given
        CreateCaseRequest request = new CreateCaseRequest();
        request.setTitle("Test Case");
        request.setDescription("Test Description");
        request.setStatus(CaseStatus.OPEN);
        request.setPriority(CasePriority.HIGH);
        request.setCategory("Test Category");
        request.setAssignedTo("testuser");
        
        // When
        Case entity = mapper.toEntity(request);
        
        // Then
        assertNotNull(entity);
        assertEquals("Test Case", entity.getTitle());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(CaseStatus.OPEN, entity.getStatus());
        assertEquals(CasePriority.HIGH, entity.getPriority());
        assertEquals("Test Category", entity.getCategory());
        assertEquals("testuser", entity.getAssignedTo());
        assertNull(entity.getId()); // Should be ignored
        assertNull(entity.getCaseNumber()); // Should be ignored
    }
    
    @Test
    @Disabled("Disabled due to missing nested mappers in plain MapStruct test context.")
    void testEntityToDto() {
        // Given
        Case entity = new Case();
        entity.setId(1L);
        entity.setCaseNumber("CASE-001");
        entity.setTitle("Test Case");
        entity.setDescription("Test Description");
        entity.setStatus(CaseStatus.OPEN);
        entity.setPriority(CasePriority.HIGH);
        
        // When
        CaseDto dto = mapper.toDto(entity);
        
        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("CASE-001", dto.getCaseNumber());
        assertEquals("Test Case", dto.getTitle());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(CaseStatus.OPEN, dto.getStatus());
        assertEquals(CasePriority.HIGH, dto.getPriority());
    }
} 