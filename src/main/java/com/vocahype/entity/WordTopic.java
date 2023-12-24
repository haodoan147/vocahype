package com.vocahype.entity;


import lombok.*;

import javax.persistence.*;
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
    private Long frequency;
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
        return word.getId().toString() + " - " + topic.getId().toString();
    }
}
