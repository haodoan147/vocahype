package com.vocahype.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {

    public String dictionaryApiUrl;
    public Long dataWord;
    public String openAiApiKey;
    public String wordsApiKey;

}
