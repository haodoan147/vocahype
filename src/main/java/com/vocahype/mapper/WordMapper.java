package com.vocahype.mapper;

import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.entity.WordUserKnowledge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WordMapper {
    @Mapping(target = "wordUserKnowledgeID.wordId", source = "wordId")
    WordUserKnowledge wordDtoToWordUserKnowledge(WordUserKnowledgeDTO wordUserKnowledgeDTO);
}

