package com.tee7even.documents.service.dto.mapper;

import com.tee7even.documents.entity.DocumentTreeNode;
import com.tee7even.documents.exception.ParentNodeNotFoundException;
import com.tee7even.documents.repository.DocumentTreeNodeRepository;
import com.tee7even.documents.service.dto.DocumentInputDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentInputMapperTest {

    static DocumentTreeNodeRepository repository;

    static DocumentInputMapper mapper;

    @BeforeAll
    static void beforeAll(@Mock DocumentTreeNodeRepository repositoryMock) {
        repository = repositoryMock;
        mapper = new DocumentInputMapper(repositoryMock);
    }

    @AfterEach
    void afterEach() {
        reset(repository);
    }

    @Test
    void toEntity_shouldMapDtoCorrectly_ifParentIdIsNull() {
        DocumentTreeNode expectedEntity = new DocumentTreeNode(null, "Test Name", "Test Content", null, List.of());
        DocumentTreeNode actualEntity = mapper.toEntity(new DocumentInputDto(null, "Test Name", "Test Content"));

        assertEquals(expectedEntity, actualEntity);
        verifyNoInteractions(repository);
    }

    @Test
    void toEntity_shouldMapDtoCorrectly_ifParentIdIsNotNull() {
        DocumentTreeNode parentNode = new DocumentTreeNode(0L, "Test Name", "Test Content", null, List.of());
        when(repository.findById(0L)).thenReturn(Optional.of(parentNode));

        DocumentTreeNode expectedEntity = new DocumentTreeNode(null, "Test Name", "Test Content", parentNode, List.of());
        DocumentTreeNode actualEntity = mapper.toEntity(new DocumentInputDto(0L, "Test Name", "Test Content"));

        assertEquals(expectedEntity, actualEntity);
        verify(repository).findById(0L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void toEntity_shouldThrowParentNodeNotFoundException_ifParentNodeIsNotNullAndIsNotFound() {
        when(repository.findById(0L)).thenReturn(Optional.empty());
        DocumentInputDto dto = new DocumentInputDto(0L, "Test Name", "Test Content");

        assertThrows(ParentNodeNotFoundException.class, () -> mapper.toEntity(dto));
        verify(repository).findById(0L);
        verifyNoMoreInteractions(repository);
    }
}
