package com.vocahype.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.converter.JsonConverter;
import com.vocahype.dto.DefinitionDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "meanings", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@NamedEntityGraph(
        name = "graph.meaning.pos",
        attributeNodes = @NamedAttributeNode("pos")
)
public class Meaning implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_meanings")
    @SequenceGenerator(name = "seq_id_meanings", sequenceName = "seq_id_meanings", allocationSize = 1, schema = "vh")
    private Long id;

    @Column(name = "definitions", columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Set<DefinitionDTO> definitions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    @JsonIgnore
    private Word word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pos_tag")
    @JsonIgnore
    private Pos pos;

//    @OneToMany(mappedBy = "meaning", fetch = FetchType.LAZY)
//    private Set<Definition> definitions;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.LAZY)
    private Set<Synonym> synonyms;

    @Override
    public String toString() {
        return id.toString();
    }
}
