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
public class SynonymID implements Serializable {
    @Column(name = "meanings_id")
    private Long meaningsId;
    @Column(name = "synonym_id")
    private Long synonymId;
}
