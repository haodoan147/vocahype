package com.vocahype.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Configuration
@RequiredArgsConstructor
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        modelMapper.typeMap(Synonym.class, SynonymDTO.class)
//                .addMapping(synonym -> synonym.getSynonymID() == null ? null : synonym.getSynonym().getId(), SynonymDTO::setId)
//                .addMapping(synonym -> synonym.getSynonym() == null ? null : synonym.getSynonym().getWord(), SynonymDTO::setSynonym);
//        modelMapper.typeMap(Definition.class, DefinitionDTO.class)
//                .addMapping(definition -> definition.getExamples() == null ? null : definition.getExamples().stream().map(example -> modelMapper.map(example, ExampleDTO.class)).collect(Collectors.toSet()), DefinitionDTO::setExamples);
//        modelMapper.typeMap(Meaning.class, MeaningDTO.class)
//                .addMapping(word -> word.getSynonyms() == null ? null : word.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym)).collect(Collectors.toSet()), WordDTO::setSynonyms)
//                .addMapping(meaning -> meaning.getDefinitions() == null ? null : meaning.getDefinitions().stream().map(definition -> modelMapper.map(definition, DefinitionDTO.class)).collect(Collectors.toSet()), MeaningDTO::setDefinitions);
//        modelMapper.typeMap(Word.class, WordDTO.class)
//                .addMapping(word -> word.getSynonyms() == null ? null : word.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym)).collect(Collectors.toSet()), WordDTO::setSynonyms)
//                .addMapping(word -> word.getMeanings() == null ? null : word.getMeanings().stream().map(meaning -> modelMapper.map(meaning, MeaningDTO.class)).collect(Collectors.toSet()), WordDTO::setMeanings);

        return modelMapper;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .failOnUnknownProperties(false);
    }

    @Bean
    public Module hibernate5Module() {
        return new Hibernate5Module();
    }

    @Bean
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}
