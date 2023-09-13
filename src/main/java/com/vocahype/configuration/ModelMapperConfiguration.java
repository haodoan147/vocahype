package com.vocahype.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.vocahype.dto.ResponseEntity;
import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Word;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.stream.Collectors;


@Configuration
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.typeMap(Word.class, WordDTO.class)
                .addMapping(Word::getDefinitions, WordDTO::setDefinitions);
//                .addMapping(word -> word.getSynonyms() == null ? null : word.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym())).collect(Collectors.toList()), WordDTO::setSynonyms);
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
