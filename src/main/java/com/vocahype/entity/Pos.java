package com.vocahype.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "pos", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pos {

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
