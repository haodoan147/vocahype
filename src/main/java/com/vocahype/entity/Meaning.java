package com.vocahype.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "meanings", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@NamedEntityGraphs({
//        @NamedEntityGraph(
//                name = "graph.WordDefinitionExample",
//                attributeNodes =  @NamedAttributeNode(value = "definitions", subgraph = "subgraph.DefinitionExample"),
//                subgraphs = {
//                        @NamedSubgraph(name = "subgraph.DefinitionExample", attributeNodes = @NamedAttributeNode(value = "examples"))
//                }
//        ),
//        @NamedEntityGraph(
//                name = "graph.WordSynonymSynonym",
//                attributeNodes =  @NamedAttributeNode(value = "synonyms", subgraph = "subgraph.SynonymSynonymID"),
//                subgraphs = {
//                        @NamedSubgraph(name = "subgraph.SynonymSynonymID", attributeNodes = @NamedAttributeNode(value = "synonym")),
//                        @NamedSubgraph(name = "subgraph.SynonymSynonymID", attributeNodes = @NamedAttributeNode(value = "synonymID"))
//                }
//        ),
//})
public class Meaning implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    @JsonIgnore
    private Word word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pos_tag")
    @JsonIgnore
    private Pos pos;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.LAZY)
    private Set<Definition> definitions;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.LAZY)
    private Set<Synonym> synonyms;

    @Override
    public String toString() {
        return id.toString();
    }
}
