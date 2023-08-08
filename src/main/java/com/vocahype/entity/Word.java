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
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "words", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "count")
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pos_tag")
    @JsonIgnore
    private Pos pos;

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

    @OneToMany(mappedBy = "word")
    @JsonIgnore
    private Set<Definition> definitions;

    @Override
    public String toString() {
        return id.toString();
    }
}
