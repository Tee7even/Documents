package com.tee7even.documents.service;

import com.tee7even.documents.entity.DocumentTreeNode;
import com.tee7even.documents.repository.DocumentTreeNodeRepository;
import com.tee7even.documents.service.dto.DocumentTreeNodeDto;
import com.tee7even.documents.service.dto.mapper.DocumentTreeNodeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    static DocumentTreeNodeRepository repository;

    static DocumentService service;

    @BeforeAll
    static void beforeAll(@Mock DocumentTreeNodeRepository repositoryMock) {
        repository = repositoryMock;
        service = new DocumentService(repositoryMock, new DocumentTreeNodeMapper(), null);
    }

    @AfterEach
    void afterEach() {
        reset(repository);
    }

    @Test
    void getTreeByRootId_shouldMapEntityListToNodeDtoTree() {
        ArrayList<DocumentTreeNode> entities = new ArrayList<>();
        entities.add(new DocumentTreeNode(0L, "Test Name", "Test Content", null, List.of()));
        entities.add(new DocumentTreeNode(1L, "Test Name", "Test Content", entities.get(0), List.of()));
        entities.add(new DocumentTreeNode(2L, "Test Name", "Test Content", entities.get(1), List.of()));
        entities.add(new DocumentTreeNode(3L, "Test Name", "Test Content", entities.get(0), List.of()));
        when(repository.findByIdWithAllChildrenNodes(0L)).thenReturn(entities);

        Optional<DocumentTreeNodeDto> expectedDto = Optional.of(
            new DocumentTreeNodeDto(0L, "Test Name", "Test Content", List.of(
                new DocumentTreeNodeDto(1L, "Test Name", "Test Content", List.of(
                    new DocumentTreeNodeDto(2L, "Test Name", "Test Content", List.of())
                )),
                new DocumentTreeNodeDto(3L, "Test Name", "Test Content", List.of())
            ))
        );
        Optional<DocumentTreeNodeDto> actualDto = service.getTreeByRootId(0L);

        assertEquals(expectedDto, actualDto);
    }
}
