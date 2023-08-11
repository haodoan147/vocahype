package com.vocahype.service;

import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.entity.User;
import com.vocahype.entity.Word;
import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import com.vocahype.repository.SynonymRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordUserKnowledgeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.vocahype.util.Constants.CURRENT_USER_ID;
import static com.vocahype.util.Constants.WORD_COUNT;

@Service
@RequiredArgsConstructor
public class SynonymService {

    private final SynonymRepository synonymRepository;

//    public List<String> get() {
//        return wordRepository.findAllById(randomIds).stream()
//                .map(word -> modelMapper.map(word, WordUserKnowledgeDTO.class))
//                .collect(Collectors.toList());
//    }

}
