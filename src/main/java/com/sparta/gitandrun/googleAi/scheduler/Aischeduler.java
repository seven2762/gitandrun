package com.sparta.gitandrun.googleAi.scheduler;

import com.sparta.gitandrun.googleAi.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@EnableScheduling
public class Aischeduler {

    private final AiService aiService;

    @Scheduled(cron = "0 0 4 * * *")
    public void DeleteOlder() {
        aiService.deleteOldQuestion();
    }
}