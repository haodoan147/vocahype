package com.vocahype.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Duration;

@Entity
@Setter
@Getter
@Table(name = "word_comprehension_levels", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordComprehensionLevel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reinforce_interval")
    @Type(type = "org.hibernate.type.DurationType")
    private Duration word;

    @Column(name = "description")
    private String description;

    @Override
    public String toString() {
        return id.toString();
    }
}
