package com.vocahype.dto.quiz;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class QuizDTO {
    private String type;
    private String question;
    private String word; // For definition select
    private Object result; // For word scramble and word guess
    private String statement; // For true-false
    private Boolean answer; // For true-false
    private List<RelatedWordSelectAnswer> relatedWordAnswers; // For related word select
    private List<AntonymSynonymMatchAnswer> antonymSynonymAnswers; // For antonym-synonym match
    private String description; // For word guess
}
