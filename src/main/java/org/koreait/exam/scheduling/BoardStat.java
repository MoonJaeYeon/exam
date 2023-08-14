package org.koreait.exam.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BoardStat {
    @Scheduled(fixedDelay = 1000)
    @Scheduled(cron="0 0 1 * * *")
    public void makeStat() {
        System.out.println("매일 1시에 실행!");
    }
}
