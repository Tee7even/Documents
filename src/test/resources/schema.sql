DROP TABLE IF EXISTS document_tree_node;

CREATE TABLE document_tree_node (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    document_name VARCHAR(255),
    document_content TEXT,
    parent_id INT REFERENCES document_tree_node(id) ON DELETE CASCADE
);
