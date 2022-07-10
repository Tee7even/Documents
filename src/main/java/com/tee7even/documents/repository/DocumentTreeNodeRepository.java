package com.tee7even.documents.repository;

import com.tee7even.documents.entity.DocumentTreeNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTreeNodeRepository extends JpaRepository<DocumentTreeNode, Long> {

    @Query(value = "WITH RECURSIVE recursive_query AS (" +
        "SELECT id, document_name, document_content, parent_id FROM document_tree_node WHERE id = :id " +
        "UNION ALL " +
        "SELECT node.id, node.document_name, node.document_content, node.parent_id " +
        "FROM document_tree_node node JOIN recursive_query cte ON cte.id = node.parent_id) " +
        "SELECT * FROM recursive_query;", nativeQuery = true)
    List<DocumentTreeNode> findByIdWithAllChildrenNodes(@Param("id") Long id);
}
