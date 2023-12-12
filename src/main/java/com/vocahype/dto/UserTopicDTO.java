package com.vocahype.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor
public class UserTopicDTO {
    private JsonNullable<Long> topicId;
}
