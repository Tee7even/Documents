package com.tee7even.documents.controller;

import com.tee7even.documents.service.dto.DocumentInputDto;
import com.tee7even.documents.service.dto.DocumentTreeNodeDto;
import com.tee7even.documents.exception.ParentNodeNotFoundException;
import com.tee7even.documents.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentsController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);

    private final DocumentService service;

    @GetMapping("/{id}/tree")
    public DocumentTreeNodeDto getDocumentTreeByRootId(@PathVariable Long id) {
        logger.info("getDocumentTreeByRootId(id={})", id);
        return service.getTreeByRootId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public DocumentIdResponse postNewDocument(@RequestBody DocumentInputDto inputDto) {
        logger.info("postNewDocument(inputDto={})", inputDto);
        try {
            Long documentId = service.addNewDocument(inputDto);
            return new DocumentIdResponse(documentId);
        } catch (ParentNodeNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public record DocumentIdResponse(long id) { }
}
