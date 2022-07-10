package com.tee7even.documents.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DocumentTreeNodeDto implements Serializable {

    private final Long id;
    private final String name;
    private final String content;
    private final List<DocumentTreeNodeDto> children;
}
