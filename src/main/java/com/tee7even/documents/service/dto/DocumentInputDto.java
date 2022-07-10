package com.tee7even.documents.service.dto;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class DocumentInputDto implements Serializable {

    @NonNull private final Long parentId;
    @NonNull private final String name;
    @NonNull private final String content;
}
