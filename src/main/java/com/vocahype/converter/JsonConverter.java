package com.vocahype.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.DefinitionDTO;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;
import java.util.Set;

@Converter
public class JsonConverter implements AttributeConverter<Set<DefinitionDTO>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<DefinitionDTO> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle conversion error
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<DefinitionDTO> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new HashSet<>();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<Set<DefinitionDTO>>() {});
        } catch (JsonProcessingException e) {
            // Handle conversion error
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}


