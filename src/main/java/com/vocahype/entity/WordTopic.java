package com.vocahype.entity;


import liquibase.pro.packaged.T;
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
@Table(name = "word_topic", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordTopic implements Serializable {
    @EmbeddedId
    private WordTopicID wordTopicID;
    @Column(name = "frequency")
    private Integer frequency;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wordId")
    @JoinColumn(name = "word_id")
    private Word word;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Override
    public String toString() {
        return wordTopicID.toString();
    }
}
