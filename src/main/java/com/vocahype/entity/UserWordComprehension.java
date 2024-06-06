package com.vocahype.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "user_word_comprehension", schema = "learning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "graph.userWordComprehension.word",
        attributeNodes = @NamedAttributeNode("word"))
public class UserWordComprehension implements Serializable {

    @EmbeddedId
    private UserWordComprehensionID userWordComprehensionID;

    @Column(name = "word_comprehension_levels_id", nullable = false)
    private Integer wordComprehensionLevel;

    @Column(name = "next_learning")
    private Timestamp nextLearning;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wordId")
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return word.getId().toString();
    }
}
