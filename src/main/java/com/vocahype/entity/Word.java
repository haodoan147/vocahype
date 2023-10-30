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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "words", schema = "vh")
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
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "graph.WordMeaningPos",
                attributeNodes =  @NamedAttributeNode(value = "meanings", subgraph = "subgraph.MeaningPos"),
                subgraphs = {
                        @NamedSubgraph(name = "subgraph.MeaningPos",
                                attributeNodes = @NamedAttributeNode(value = "pos"))
                }
        ),
})
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "count")
    private Long count;

    @Column(name = "point")
    private Double point;

    @Column(name = "phonetic")
    private String phonetic;

    @Column(name = "syllables")
    private Integer syllable;

    @Column(name = "phonetic_start")
    private String phoneticStart;

    @Column(name = "phonetic_end")
    private String phoneticEnd;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private Set<Meaning> meanings;

    @OneToMany(mappedBy = "synonym", fetch = FetchType.LAZY)
    private Set<Synonym> synonyms;

    @Override
    public String toString() {
        return id.toString();
    }
}
