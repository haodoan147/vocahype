package com.vocahype.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "definition", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Definition implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_definition")
    @SequenceGenerator(name = "seq_id_definition", sequenceName = "seq_id_definition", allocationSize = 1, schema = "vh")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "meanings_id")
    private Meaning meaning;

    @Column(name = "definition")
    private String definition;

    @OneToMany(mappedBy = "definition")
    @JsonIgnore
    private Set<Example> examples;

    @Override
    public String toString() {
        return id.toString();
    }
}
