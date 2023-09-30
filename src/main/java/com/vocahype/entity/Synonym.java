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
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "graph.SynonymWord",
                attributeNodes =  @NamedAttributeNode(value = "word")
        )
})
public class Synonym implements Serializable {
    @Id
    @EmbeddedId
    private SynonymID synonymID;
    @Column(name = "is_synonym")
    private Boolean isSynonym;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wordId")
    @JoinColumn(name = "word_id")
    private Word word;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("synonymId")
    @JoinColumn(name = "synonym_id")
    private Word synonym;

    @Override
    public String toString() {
        return synonymID.getSynonymId().toString();
    }

}
