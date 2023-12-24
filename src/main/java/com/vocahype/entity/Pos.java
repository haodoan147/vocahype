package com.vocahype.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "pos", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pos implements Serializable {

    @Id
    @Column(name = "pos_tag")
    private String posTag;

    @Column(name = "description")
    private String description;

    @Override
    public String toString() {
        return posTag;
    }
}
