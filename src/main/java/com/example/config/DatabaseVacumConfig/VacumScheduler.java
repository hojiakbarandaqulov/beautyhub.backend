package com.example.config.DatabaseVacumConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class VacumScheduler {

    private final JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 */5 * * * *")
    public void vacuumDb() {
        jdbcTemplate.execute("VACUUM");
        log.info("Vacuum DB started");
        System.out.println("VACUUM bajarildi: " + java.time.LocalDateTime.now());
    }

}