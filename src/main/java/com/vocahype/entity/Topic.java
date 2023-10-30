package com.vocahype.entity;


import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "topics", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Topic implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_topics")
    @SequenceGenerator(name = "seq_id_topics", sequenceName = "seq_id_topics", allocationSize = 1, schema = "vh")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "emoji")
    private String emoji;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    private Set<WordTopic> wordTopics;

    @Override
    public String toString() {
        return id.toString();
    }
}
