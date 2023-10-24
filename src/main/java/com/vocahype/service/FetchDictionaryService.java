package com.vocahype.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Word;
import com.vocahype.repository.DefinitionRepository;
import com.vocahype.repository.PartitionLearningsRepositoryCustomImpl;
import com.vocahype.repository.PosRepository;
import com.vocahype.repository.WordRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static com.vocahype.util.ConversionUtils.roundDown;

@Log4j2
@Service
@RequiredArgsConstructor
public class FetchDictionaryService {
    private final WordRepository wordRepository;
    private final DefinitionRepository definitionRepository;
    private final PosRepository posRepository;
    @Qualifier("dictionaryApiWebClient")
    private final WebClient webClient;

    public void fetchDictionary(final String wordToFind) {
        List<DictionaryEntry> dictionaryEntries = webClient.get().uri("/" + wordToFind).retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                    log.error("Error: " + clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Error: " + clientResponse.statusCode()));
                })
                .bodyToMono(new ParameterizedTypeReference<List<DictionaryEntry>>() {
                })
                .block();
        dictionaryEntries.forEach(dictionaryEntry -> {
            Word word = wordRepository.findByWordIgnoreCase(dictionaryEntry.getWord()).orElse(new Word());
            if (word.getWord() == null) {
                word.setWord(dictionaryEntry.getWord());
            }
            if (!dictionaryEntry.getMeanings().isEmpty()) {
                posRepository.findById(ConvertPos.valueOf(dictionaryEntry.getMeanings().stream().findFirst().get()
                                .getPartOfSpeech().toUpperCase()).getTitle())
                        .ifPresent(word::setPos);
            }
            if (!dictionaryEntry.getPhonetics().isEmpty()) {
                word.setPhonetic(dictionaryEntry.getPhonetics().stream().findFirst().orElse(new Phonetic()).getText());
            }
            if (!dictionaryEntry.getMeanings().isEmpty()) {
                if (word.getDefinitions() == null) {
//                    word.setDefinition(dictionaryEntry.getMeanings().get(0).getDefinitions().get(0).getDefinition());
//                    word.setExample(dictionaryEntry.getMeanings().get(0).getDefinitions().get(0).getExample());
                }
            }
        });
    }
}

@Data
class License {
    private String name;
    private String url;
}

@Data
class Phonetic {
    private String text;
    @JsonProperty("sourceUrl")
    private String sourceUrl;
    private String audio;
    private License license;
}

@Data
class Definition {
    private String definition;
    private String example;
    private List<String> synonyms;
    private List<String> antonyms;

    com.vocahype.entity.Definition toDefinition() {
        com.vocahype.entity.Definition definition = new com.vocahype.entity.Definition();
        definition.setDefinition(this.definition);
        definition.setExamples(Set.of(new com.vocahype.entity.Example().setExample(this.example)));
        return definition;
    }
}
@Data
class Meaning {
    @JsonProperty("partOfSpeech")
    private String partOfSpeech;
    private List<Definition> definitions;
    private List<String> synonyms;
    private List<String> antonyms;

}
@Data
class DictionaryEntry {
    private String word;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;
    private License license;
    @JsonProperty("sourceUrls")
    private List<String> sourceUrls;

    Word toWord() {
        Word word = new Word();
        word.setWord(this.word);
        word.setPhonetic(this.phonetics.get(0).getText());
//        word.setPhoneticAudio(this.phonetics.get(0).getAudio());
//        word.setPhoneticSourceUrl(this.phonetics.get(0).getSourceUrl());
//        word.setPos(this.meanings.get(0).getPartOfSpeech());
//        word.setDefinition(this.meanings.get(0).getDefinitions().get(0).getDefinition());
//        word.setExample(this.meanings.get(0).getDefinitions().get(0).getExample());
//        word.setSynonyms(this.meanings.get(0).getDefinitions().get(0).getSynonyms());
//        word.setAntonyms(this.meanings.get(0).getDefinitions().get(0).getAntonyms());
//        word.setSourceUrl(this.sourceUrls.get(0));
        return word;
    }
}

enum ConvertPos {
    VERB("VB");

    private final String title;

    ConvertPos(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

