package com.tee7even.documents.repository;

import com.tee7even.documents.entity.DocumentTreeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("postgres-integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DocumentTreeNodeRepositoryTest {

    static final long ROOT_NODE_ID = 1;
    static final long TOTAL_NODES = 10;

    static Stream<Long> expectedIdProvider() {
        return LongStream.rangeClosed(ROOT_NODE_ID, TOTAL_NODES).boxed();
    }

    @Autowired
    DocumentTreeNodeRepository repository;

    @Test
    void findByIdWithAllChildrenNodes_shouldReturnAListOfExpectedSize() {
        List<DocumentTreeNode> nodeList = repository.findByIdWithAllChildrenNodes(ROOT_NODE_ID);
        assertEquals(TOTAL_NODES, nodeList.size());
    }

    @ParameterizedTest
    @MethodSource("expectedIdProvider")
    void findByIdWithAllChildrenNodes_shouldReturnAListThatContainsExpectedValues(Long id) {
        List<DocumentTreeNode> nodeList = repository.findByIdWithAllChildrenNodes(ROOT_NODE_ID);
        assertTrue(nodeList.stream().anyMatch(node -> node.getId().equals(id)));
    }
}
