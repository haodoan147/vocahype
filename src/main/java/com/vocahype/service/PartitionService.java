package com.vocahype.service;

import com.vocahype.repository.PartitionLearningsRepositoryCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.vocahype.util.ConversionUtils.roundDown;

@Log4j2
@Service
@RequiredArgsConstructor
public class PartitionService {
    private final PartitionLearningsRepositoryCustomImpl partitionLearningsRepositoryCustom;

    public void createMeterPartitions() {
        int userCount = -1001;
        partitionLearningsRepositoryCustom.createLearningPartition(
                "learning",
                "user_word_comprehension",
                roundDown(userCount, 1000) + 1000);
    }
}
