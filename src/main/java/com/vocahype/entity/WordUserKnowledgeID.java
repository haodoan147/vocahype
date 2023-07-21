package com.vocahype.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class WordUserKnowledgeID implements Serializable {

    private Long wordId;
    private Long userId;
}
