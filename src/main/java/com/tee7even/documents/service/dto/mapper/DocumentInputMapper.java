package com.tee7even.documents.service.dto.mapper;

import com.tee7even.documents.service.dto.DocumentInputDto;
import com.tee7even.documents.entity.DocumentTreeNode;
import com.tee7even.documents.exception.ParentNodeNotFoundException;
import com.tee7even.documents.repository.DocumentTreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentInputMapper {

    private final DocumentTreeNodeRepository repository;

    public DocumentTreeNode toEntity(DocumentInputDto dto) throws ParentNodeNotFoundException {
        return DocumentTreeNode.builder()
            .name(dto.getName())
            .content(dto.getContent())
            .parent(dto.getParentId() == null ? null
                : repository.findById(dto.getParentId()).orElseThrow(ParentNodeNotFoundException::new))
            .build();
    }
}
