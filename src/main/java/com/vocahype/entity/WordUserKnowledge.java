package com.vocahype.entity;


import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "words_user_knowledge", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordUserKnowledge {
    @EmbeddedId
    private WordUserKnowledgeID wordUserKnowledgeID;
    @Column(name = "status")
    private Boolean status;
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
        return wordUserKnowledgeID.toString();
    }
}
