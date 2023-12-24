package com.vocahype.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "synonyms", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@NamedEntityGraphs({
//        @NamedEntityGraph(
//                name = "graph.SynonymWord",
//                attributeNodes =  @NamedAttributeNode(value = "meaning", subgraph = "subgraph.MeaningWord"),
//        )
//})
@NamedEntityGraph(
        name = "graph.SynonymMeaningsWord",
        attributeNodes =  @NamedAttributeNode(value = "meaning", subgraph = "subgraph.MeaningsWord"),
        subgraphs = {
                @NamedSubgraph(name = "subgraph.MeaningsWord", attributeNodes = @NamedAttributeNode(value = "word"))
        }
)
public class Synonym implements Serializable {
    @Id
    @EmbeddedId
    private SynonymID synonymID;
    @Column(name = "is_synonym")
    private Boolean isSynonym;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("meaningsId")
    @JoinColumn(name = "meanings_id")
    private Meaning meaning;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("synonymId")
    @JoinColumn(name = "synonym_id")
    private Word synonym;

    @Override
    public String toString() {
        return synonymID.getSynonymId().toString();
    }

}
