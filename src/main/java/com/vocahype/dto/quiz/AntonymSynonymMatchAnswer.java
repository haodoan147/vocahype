package com.vocahype.dto.quiz;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class AntonymSynonymMatchAnswer {
    private String text;
    private boolean isSynonym;
    private boolean isAntonym;
}
