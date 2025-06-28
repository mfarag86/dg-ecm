package com.enterprise.ecm.cases.mapper;

import com.enterprise.ecm.cases.CaseNote;
import com.enterprise.ecm.cases.dto.CaseNoteDto;
import com.enterprise.ecm.cases.dto.CreateCaseNoteRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseNoteMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "caseEntity", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    CaseNote toEntity(CreateCaseNoteRequest request);
    
    CaseNoteDto toDto(CaseNote entity);
    
    List<CaseNoteDto> toDtoList(List<CaseNote> entities);
} 