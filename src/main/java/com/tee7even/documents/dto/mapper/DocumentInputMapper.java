package com.tee7even.documents.dto.mapper;

import com.tee7even.documents.dto.DocumentInputDto;
import com.tee7even.documents.entity.DocumentTreeNode;
import com.tee7even.documents.exception.ParentNodeNotFoundException;
import com.tee7even.documents.repository.DocumentTreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentInputMapper {

    private final DocumentTreeNodeRepository repository;

    public DocumentTreeNode toEntity(DocumentInputDto dto) {
        return DocumentTreeNode.builder()
            .name(dto.getName())
            .content(dto.getContent())
            .parent(repository.findById(dto.getParentId()).orElseThrow(ParentNodeNotFoundException::new))
            .build();
    }
}
