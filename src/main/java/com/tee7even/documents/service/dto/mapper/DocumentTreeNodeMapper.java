package com.tee7even.documents.service.dto.mapper;

import com.tee7even.documents.service.dto.DocumentTreeNodeDto;
import com.tee7even.documents.entity.DocumentTreeNode;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class DocumentTreeNodeMapper {

    public DocumentTreeNodeDto toDto(DocumentTreeNode entity) {
        return new DocumentTreeNodeDto(entity.getId(), entity.getName(), entity.getContent(), new LinkedList<>());
    }
}
