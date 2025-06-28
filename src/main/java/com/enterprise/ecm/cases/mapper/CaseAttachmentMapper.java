package com.enterprise.ecm.cases.mapper;

import com.enterprise.ecm.cases.CaseAttachment;
import com.enterprise.ecm.cases.dto.CaseAttachmentDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseAttachmentMapper {
    
    CaseAttachmentDto toDto(CaseAttachment entity);
    
    List<CaseAttachmentDto> toDtoList(List<CaseAttachment> entities);
} 