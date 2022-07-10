package com.tee7even.documents.controller;

import com.tee7even.documents.exception.ParentNodeNotFoundException;
import com.tee7even.documents.service.DocumentService;
import com.tee7even.documents.service.dto.DocumentTreeNodeDto;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentsController.class)
class DocumentsControllerTest {

    static final long NOT_FOUND_ID = 10;
    static final long FOUND_ID = 0;

    static final String GET_EXPECTED_JSON = """
        {
            "id": 0,
            "name": "Test Name #0",
            "content": "Content",
            "children": [
                {
                    "id": 1,
                    "name": "Test Name #1",
                    "content": "Content",
                    "children": []
                }
            ]
        }""";

    static final String POST_EXPECTED_JSON = """
        {
            "id": 0
        }""";

    static final String POST_REQUEST_JSON = """
        {
            "parentId": %d,
            "name": "",
            "content": ""
        }""";

    @MockBean
    DocumentService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getDocumentTreeByRootId_shouldReturnHTTP404_ifDocumentIsNotFound() throws Exception {
        when(service.getTreeByRootId(NOT_FOUND_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(String.format("/documents/%d/tree", NOT_FOUND_ID)).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(service).getTreeByRootId(NOT_FOUND_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDocumentTreeByRootId_shouldReturnHTTP200AndDocumentAndItsChildren_ifDocumentIsFound() throws Exception {
        when(service.getTreeByRootId(FOUND_ID)).thenReturn(Optional.of(
            new DocumentTreeNodeDto(0L, "Test Name #0", "Content", List.of(
                new DocumentTreeNodeDto(1L, "Test Name #1", "Content", List.of())
            )))
        );

        MvcResult result = mockMvc.perform(get(String.format("/documents/%d/tree", FOUND_ID)).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        JSONAssert.assertEquals(GET_EXPECTED_JSON, result.getResponse().getContentAsString(), false);
        verify(service).getTreeByRootId(FOUND_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void postNewDocument_shouldReturnHTTP422_ifParentNodeIsNotFound() throws Exception {
        when(service.addNewDocument(argThat(dto -> dto.getParentId().equals(NOT_FOUND_ID)))).thenThrow(ParentNodeNotFoundException.class);

        mockMvc.perform(post("/documents/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.format(POST_REQUEST_JSON, NOT_FOUND_ID)))
            .andExpect(status().isUnprocessableEntity());

        verify(service).addNewDocument(argThat(dto -> dto.getParentId().equals(NOT_FOUND_ID)));
        verifyNoMoreInteractions(service);
    }

    @Test
    void postNewDocument_shouldReturnHTTP200AndCreatedDocumentId_ifDocumentIsCreatedSuccessfully() throws Exception {
        when(service.addNewDocument(argThat(dto -> dto.getParentId().equals(FOUND_ID)))).thenReturn(0L);

        MvcResult result = mockMvc.perform(post("/documents/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.format(POST_REQUEST_JSON, FOUND_ID)))
            .andExpect(status().isOk())
            .andReturn();

        JSONAssert.assertEquals(POST_EXPECTED_JSON, result.getResponse().getContentAsString(), false);
        verify(service).addNewDocument(argThat(dto -> dto.getParentId().equals(FOUND_ID)));
        verifyNoMoreInteractions(service);
    }
}
