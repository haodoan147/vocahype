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
//    @JoinColumn(name = "word_id", insertable = false, updatable = false)
//    @ManyToOne
//    private Word word;
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    @ManyToOne
//    private User user;

    @Override
    public String toString() {
        return wordUserKnowledgeID.toString();
    }
}
