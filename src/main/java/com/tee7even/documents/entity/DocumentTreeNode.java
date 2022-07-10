package com.tee7even.documents.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_tree_node")
public class DocumentTreeNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_name")
    private String name;

    @Column(name = "document_content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DocumentTreeNode parent;

    @OneToMany(mappedBy = "parent")
    private List<DocumentTreeNode> children;
}
