package com.nytour.demo.task;

import com.nytour.demo.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Message Scheduled Task - Runs every minute
 * 
 * MIGRATED FROM:
 * - Field injection → Constructor injection
 * - Log4j 1.x → SLF4J
 * - SimpleDateFormat → DateTimeFormatter
 * - Date/Calendar APIs → java.time.LocalDateTime
 * - Removed deprecated finalize() method
 */
@Component
public class MessageScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(MessageScheduledTask.class);

    private final MessageService messageService;
    
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructor injection (modern pattern)
    public MessageScheduledTask(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Scheduled task that runs every 60 seconds (every minute)
     * This simulates the console app pattern from the .NET workshop
     */
    @Scheduled(fixedDelay = 60000) // 60000ms = 60 seconds = 1 minute
    public void reportMessageStatistics() {
        logger.info("========================================");
        logger.info("Message Statistics Task - Executing");
        logger.info("========================================");

        try {
            LocalDateTime now = LocalDateTime.now();
            logger.info("Execution Time: {}", now.format(dateTimeFormatter));

            // Get message statistics
            long totalMessages = messageService.getAllMessages().size();
            Long activeMessages = messageService.getActiveMessageCount();

            logger.info("Total Messages: {}", totalMessages);
            logger.info("Active Messages: {}", activeMessages);
            logger.info("Inactive Messages: {}", (totalMessages - activeMessages));

            logger.info("Messages from last 7 days: {}", 
                messageService.getRecentMessages(7).size());

            // Log next execution time using LocalDateTime
            LocalDateTime nextExecution = now.plusMinutes(1);
            logger.info("Next Execution: {}", nextExecution.format(dateTimeFormatter));

            int statusCode = 200;
            logger.info("Task Status Code: {}", statusCode);

            logger.info("Task completed successfully");

        } catch (Exception e) {
            logger.error("Error executing scheduled task", e);
            logger.error("Error message: {}", e.getMessage());
        }

        logger.info("========================================");
    }

    /**
     * Alternative scheduling method - using cron expression
     * Commented out but shows another scheduling approach
     */
    // @Scheduled(cron = "0 * * * * *") // Every minute at 0 seconds
    public void reportMessageStatisticsWithCron() {
        // Same logic as above, but triggered by cron expression
        reportMessageStatistics();
    }

    /**
     * Helper method to calculate time until next run
     */
    private String getTimeUntilNextRun() {
        LocalDateTime now = LocalDateTime.now();
        int secondsIntoMinute = now.getSecond();
        int secondsUntilNextMinute = 60 - secondsIntoMinute;
        
        return secondsUntilNextMinute + " seconds";
    }
}
