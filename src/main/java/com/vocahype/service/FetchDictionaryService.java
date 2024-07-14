package com.vocahype.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocahype.entity.Pos;
import com.vocahype.entity.SynonymID;
import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
public class FetchDictionaryService {
    private final WordRepository wordRepository;
    private final DefinitionRepository definitionRepository;
    private final PosRepository posRepository;
    private final WebClient webClient;
    private final MeaningRepository meaningRepository;
    private final ExampleRepository exampleRepository;
    private final SynonymRepository synonymRepository;

    public FetchDictionaryService(final WordRepository wordRepository, final DefinitionRepository definitionRepository,
                                  final PosRepository posRepository,
                                  final @Qualifier("dictionaryApiWebClient") WebClient webClient,
                                  final MeaningRepository meaningRepository, final ExampleRepository exampleRepository,
                                  final SynonymRepository synonymRepository) {
        this.wordRepository = wordRepository;
        this.definitionRepository = definitionRepository;
        this.posRepository = posRepository;
        this.webClient = webClient;
        this.meaningRepository = meaningRepository;
        this.exampleRepository = exampleRepository;
        this.synonymRepository = synonymRepository;
    }

    @Transactional
    public void fetchDictionary(final String word) {
        Optional<Word> byWordIgnoreCaseOrderById = wordRepository.findByWordIgnoreCaseOrderById(word);
        if (byWordIgnoreCaseOrderById.isEmpty()) {
            throw new InvalidException("Word not found", "Word not found: " + word);
        }
        fetchDictionary(byWordIgnoreCaseOrderById.get());
    }

    @Transactional
    public void fetchDictionary(final Long id, final Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        List<Word> byWordIgnoreCaseOrderById = wordRepository.findByCountIsNotNullAndIdGreaterThanOrderById(id, pageable);
        byWordIgnoreCaseOrderById.forEach(this::fetchDictionary);
    }

    public void fetchDictionary(final Word wordToFind) {
        log.info("Fetching word: " + wordToFind.getWord() + " id: " + wordToFind.getId());
        List<DictionaryEntry> dictionaryEntries = webClient.get()
                .uri("/" + wordToFind.getWord())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            log.info("Word: " + wordToFind.getWord());
                            return clientResponse.bodyToMono(String.class).then(Mono.empty());
                        }
                )
                .bodyToMono(new ParameterizedTypeReference<List<DictionaryEntry>>() {})
                .defaultIfEmpty(Collections.emptyList()) // Return an empty list if no data is received
                .block();

        dictionaryEntries.forEach(dictionaryEntry -> {
            if (!dictionaryEntry.getPhonetics().isEmpty() && dictionaryEntry.getPhonetics().stream().findFirst().get().getText() != null) {
                wordToFind.setPhonetic(dictionaryEntry.getPhonetics().stream().findFirst().get().getText().replace("/", ""));
            }
            Word save = wordRepository.save(wordToFind);

            if (!dictionaryEntry.getMeanings().isEmpty()) {
                dictionaryEntry.getMeanings().forEach(meaning -> {
                    String title = ConvertPos.valueOf(meaning.getPartOfSpeech().toUpperCase().replace(" ", "_")).getTitle();
                    if (!wordToFind.getMeanings().isEmpty()) {
                        wordToFind.getMeanings().forEach(meaning1 -> {
                            if (title.equals(meaning1.getPos().getPosTag())) {
                                exampleRepository.deleteAllByDefinition_Meaning_Id(meaning1.getId());
                                definitionRepository.deleteAllByMeaning_Id(meaning1.getId());
                                synonymRepository.deleteAllByMeaning_Id(meaning1.getId());
                                meaningRepository.deleteAllById(meaning1.getId());
                            }
                        });
                    }
                    com.vocahype.entity.Meaning vhMeaning = new com.vocahype.entity.Meaning();
                    Pos posNotFound = posRepository.findById(title).orElseThrow(() -> new InvalidException("Pos not found", "Pos not found: " + meaning.getPartOfSpeech()));
                    vhMeaning.setPos(posNotFound);
                    vhMeaning.setWord(save);
                    vhMeaning = meaningRepository.save(vhMeaning);
                    if (vhMeaning.getId() == null) {
                        throw new InvalidException("Meaning not found", "Meaning not found: " + meaning.getPartOfSpeech());
                    }

                    if (!meaning.getDefinitions().isEmpty()) {
                        com.vocahype.entity.Meaning finalVhMeaning = vhMeaning;
                        meaning.getDefinitions().forEach(definition -> {
                            com.vocahype.entity.Definition vhDefinition = definition.toDefinition();
                             vhDefinition.setMeaning(finalVhMeaning);
                            vhDefinition.setDefinition(definition.getDefinition());
                            definitionRepository.save(vhDefinition);

                            if (definition.getExample() != null) {
                                com.vocahype.entity.Example vhExample = new com.vocahype.entity.Example();
                                vhExample.setDefinition(vhDefinition);
                                vhExample.setExample(definition.getExample());
//                            exampleRepository.insertExample(vhDefinition.getId(), definition.getExample());
                                exampleRepository.save(vhExample);
                            }
                         });
                    }

                    try {
                        if (!meaning.getSynonyms().isEmpty()) {
                            com.vocahype.entity.Meaning finalVhMeaning1 = vhMeaning;
                            meaning.getSynonyms().forEach(synonym -> {
                            com.vocahype.entity.Synonym vhSynonym = new com.vocahype.entity.Synonym();
                            vhSynonym.setSynonymID(new SynonymID(finalVhMeaning1.getId(), wordToFind.getId()));
                            vhSynonym.setMeaning(finalVhMeaning1);
                            vhSynonym.setSynonym(wordToFind);
                            vhSynonym.setIsSynonym(true);
                            synonymRepository.save(vhSynonym);
//                                synonymRepository.insertSynonym(wordToFind.getId(), true, finalVhMeaning1.getId());
                            });
                        }

                        if (!meaning.getAntonyms().isEmpty()) {
                            com.vocahype.entity.Meaning finalVhMeaning2 = vhMeaning;
                            meaning.getAntonyms().forEach(antonym -> {
                            com.vocahype.entity.Synonym vhSynonym = new com.vocahype.entity.Synonym();
                            vhSynonym.setSynonymID(new SynonymID(finalVhMeaning2.getId(), wordToFind.getId()));
                            vhSynonym.setMeaning(finalVhMeaning2);
                            vhSynonym.setSynonym(wordToFind);
                            vhSynonym.setIsSynonym(false);
                            synonymRepository.save(vhSynonym);
//                                synonymRepository.insertSynonym(wordToFind.getId(), false, finalVhMeaning2.getId());
                            });
                        }
                    } catch (GenericJDBCException | EntityNotFoundException e) {
                        log.error("Error saving synonyms/antonyms for word: " + vhMeaning.getId() + " synonym: " + wordToFind.getId());
                    }
                });
            }
        });
    }

//    @Transactional
//    public com.vocahype.entity.Meaning saveMeaning(com.vocahype.entity.Meaning meaning) {
//        return meaningRepository.save(meaning);
//    }
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
    VERB("VB"),
    NOUN("NN"),
    ADJECTIVE("JJ"),
    ADVERB("RB"),
    PRONOUN("PR"),
    PREPOSITION("IN"),
    CONJUNCTION("CC"),
    INTERJECTION("UH"),
    DETERMINER("DT"),
    PHRASE("PHRASE"),
    PROPER_NOUN("NNP"),
    NUMERAL("CD");

    private final String title;

    ConvertPos(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

