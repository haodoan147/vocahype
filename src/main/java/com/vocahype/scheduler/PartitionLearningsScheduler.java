package com.vocahype.scheduler;

import com.vocahype.service.PartitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Log4j2
@Component
@RequiredArgsConstructor
public class PartitionLearningsScheduler {

    @Value("${cronjob.scheduler.partitionLearning.active}")
    private boolean isActive;
    private final PartitionService partitionService;

//    @Async
//    @Scheduled(cron = "${cronjob.scheduler.partitionLearning.time}")
//    @PostConstruct
    public void createMeterNextPartitions() {
        if (!isActive) return;
        log.info("Learning Partitionings start...");
        partitionService.createMeterPartitions();
        log.info("Learning Partitionings completed!");
    }

}
