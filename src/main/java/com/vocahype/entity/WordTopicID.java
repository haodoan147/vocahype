package com.vocahype.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class WordTopicID implements Serializable {
    @Column(name = "word_id")
    private Long wordId;
    @Column(name = "topic_id")
    private Long topicId;

    @Override
    public String toString() {
        return wordId.toString() + " - " + topicId.toString();
    }
}
