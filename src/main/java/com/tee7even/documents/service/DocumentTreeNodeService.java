package com.tee7even.documents.service;

import com.tee7even.documents.dto.DocumentIdDto;
import com.tee7even.documents.dto.DocumentInputDto;
import com.tee7even.documents.dto.DocumentTreeNodeDto;
import com.tee7even.documents.dto.mapper.DocumentInputMapper;
import com.tee7even.documents.dto.mapper.DocumentTreeNodeMapper;
import com.tee7even.documents.entity.DocumentTreeNode;
import com.tee7even.documents.repository.DocumentTreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentTreeNodeService {

    private final DocumentTreeNodeRepository repository;
    private final DocumentTreeNodeMapper nodeMapper;
    private final DocumentInputMapper inputMapper;

    public Optional<DocumentTreeNodeDto> getTreeByRootId(Long id) {
        List<DocumentTreeNode> nodeList = repository.findByIdWithAllChildrenNodes(id);
        Map<Long, DocumentTreeNodeDto> dtoMap = new HashMap<>();

        nodeList.stream().map(nodeMapper::toDto).forEach(dto -> dtoMap.put(dto.getId(), dto));
        nodeList.forEach(node -> {
            DocumentTreeNode parent = node.getParent();
            if (parent != null && dtoMap.containsKey(parent.getId())) {
                DocumentTreeNodeDto parentDto = dtoMap.get(parent.getId());
                DocumentTreeNodeDto currentDto = dtoMap.get(node.getId());

                parentDto.getChildren().add(currentDto);
            }
        });

        return Optional.ofNullable(dtoMap.get(id));
    }

    public DocumentIdDto addNewDocument(DocumentInputDto inputDto) {
        DocumentTreeNode node = repository.save(inputMapper.toEntity(inputDto));
        return new DocumentIdDto(node.getId());
    }
}
