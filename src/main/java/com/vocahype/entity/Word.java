package com.vocahype.entity;


import lombok.*;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private Set<WordTopic> wordTopics;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private Set<Meaning> meanings;

    @OneToMany(mappedBy = "synonym", fetch = FetchType.LAZY)
    private Set<Synonym> synonyms;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private Set<UserWordComprehension> userWordComprehensions;

    @Override
    public String toString() {
        return id.toString();
    }
}
