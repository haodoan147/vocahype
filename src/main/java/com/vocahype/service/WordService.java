package com.vocahype.service;

import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
    }

    public List<Word> getWordsByWord(String word) {
        return wordRepository.findByWordContainsIgnoreCase(word);
    }
}
