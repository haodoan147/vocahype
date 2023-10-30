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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "topics", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_topics")
    @SequenceGenerator(name = "seq_id_topics", sequenceName = "seq_id_topics", allocationSize = 1, schema = "vh")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    private Set<Word> word;

    @Override
    public String toString() {
        return id.toString();
    }
}
