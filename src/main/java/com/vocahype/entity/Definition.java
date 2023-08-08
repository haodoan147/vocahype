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
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "definition", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Definition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "definition")
    private String definition;

    @OneToMany(mappedBy = "definition", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Example> examples;

    @Override
    public String toString() {
        return id.toString();
    }
}
