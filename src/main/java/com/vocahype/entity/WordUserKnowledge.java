package com.vocahype.entity;


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

@Entity
@Setter
@Getter
@Table(name = "words_user_knowledge", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordUserKnowledge implements Serializable {
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
