package com.vocahype.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "examples", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Example implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_examples")
    @SequenceGenerator(name = "seq_id_examples", sequenceName = "seq_id_examples", allocationSize = 1, schema = "vh")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "definition_id")
    private Definition definition;

    @Column(name = "example")
    private String example;

    @Override
    public String toString() {
        return id.toString();
    }
}
