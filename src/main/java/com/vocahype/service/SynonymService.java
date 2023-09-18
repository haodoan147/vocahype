package com.vocahype.service;

import com.vocahype.repository.SynonymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
