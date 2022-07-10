package com.tee7even.documents.service.dto.mapper;

import com.tee7even.documents.entity.DocumentTreeNode;
import com.tee7even.documents.service.dto.DocumentTreeNodeDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentTreeNodeMapperTest {

    DocumentTreeNodeMapper mapper = new DocumentTreeNodeMapper();

    @Test
    void toDto_shouldNotMapNodeChildrenAndReturnAnEmptyList() {
        DocumentTreeNodeDto expectedDto = new DocumentTreeNodeDto(0L, "Test Name", "Test Content", List.of());
        DocumentTreeNodeDto actualDto = mapper.toDto(DocumentTreeNode.builder()
            .id(0L)
            .name("Test Name")
            .content("Test Content")
            .children(List.of(new DocumentTreeNode()))
            .build());

        assertEquals(expectedDto, actualDto);
    }
}
