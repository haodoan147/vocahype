package com.vocahype.mapper;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.WordUserKnowledge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WordMapper {
    @Mapping(target = "wordUserKnowledgeID.wordId", source = "wordId")
    WordUserKnowledge wordDtoToWordUserKnowledge(WordDTO wordDTO);
}

