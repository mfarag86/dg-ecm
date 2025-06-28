package com.enterprise.ecm.cases.mapper;

import com.enterprise.ecm.cases.Case;
import com.enterprise.ecm.cases.dto.CaseDto;
import com.enterprise.ecm.cases.dto.CreateCaseRequest;
import com.enterprise.ecm.cases.dto.UpdateCaseRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CaseNoteMapper.class, CaseAttachmentMapper.class})
public interface CaseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "caseNumber", ignore = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Case toEntity(CreateCaseRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "caseNumber", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromRequest(UpdateCaseRequest request, @MappingTarget Case entity);
    
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "attachments", source = "attachments")
    CaseDto toDto(Case entity);
    
    List<CaseDto> toDtoList(List<Case> entities);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "caseNumber", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromRequestIgnoreNull(UpdateCaseRequest request, @MappingTarget Case entity);
} 