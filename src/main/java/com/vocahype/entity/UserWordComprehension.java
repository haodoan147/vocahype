package com.vocahype.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "user_word_comprehension", schema = "learning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWordComprehension implements Serializable {

    @EmbeddedId
    private UserWordComprehensionID userWordComprehensionID;

    @Column(name = "word_comprehension_levels_id", nullable = false)
    private Integer wordComprehensionLevel;

    @Column(name = "next_learning")
    private Timestamp nextLearning;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wordId")
    @JoinColumn(name = "word_id")
    private Word word;

//    @MapsId("userId")
//    @Column(name = "user_id")
//    private String user;

    @Override
    public String toString() {
        return word.getId().toString();
    }
}
