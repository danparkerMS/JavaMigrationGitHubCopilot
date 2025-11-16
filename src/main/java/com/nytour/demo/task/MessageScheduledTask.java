package com.nytour.demo.task;

import com.nytour.demo.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Message Scheduled Task - Runs every minute
 * 
 * MIGRATION CHALLENGES:
 * 1. Field injection (@Autowired on fields) instead of constructor injection
 * 2. Log4j 1.x (deprecated) will migrate to SLF4J or Logback
 * 3. SimpleDateFormat (not thread-safe) will migrate to DateTimeFormatter
 * 4. Date and Calendar APIs (deprecated) will migrate to java.time (LocalDateTime, Instant)
 * 5. Fixed delay scheduling may change to more flexible cron expressions
 * 6. Manual date arithmetic using Calendar instead of Duration/Period
 */
@Component
public class MessageScheduledTask {

    // Log4j 1.x (deprecated)
    private static final Logger logger = Logger.getLogger(MessageScheduledTask.class);

    // Field injection (legacy pattern, constructor injection preferred in modern Spring)
    @Autowired
    private MessageService messageService;

    // SimpleDateFormat (not thread-safe, should be replaced with DateTimeFormatter)
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            // Get current timestamp using deprecated Date API
            Date now = new Date();
            logger.info("Execution Time: " + dateFormat.format(now));

            // Get message statistics
            Long totalMessages = Long.valueOf(messageService.getAllMessages().size());
            Long activeMessages = messageService.getActiveMessageCount();

            logger.info("Total Messages: " + totalMessages);
            logger.info("Active Messages: " + activeMessages);
            logger.info("Inactive Messages: " + (totalMessages - activeMessages));

            // Calculate messages from last 7 days using deprecated Calendar API
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            Date sevenDaysAgo = calendar.getTime();

            logger.info("Messages from last 7 days: " + 
                messageService.getRecentMessages(7).size());

            // Log next execution time using Calendar
            Calendar nextExecution = Calendar.getInstance();
            nextExecution.setTime(now);
            nextExecution.add(Calendar.MINUTE, 1);
            logger.info("Next Execution: " + dateFormat.format(nextExecution.getTime()));

            // Deprecated Integer constructor usage
            Integer statusCode = new Integer(200);
            logger.info("Task Status Code: " + statusCode);

            logger.info("Task completed successfully");

        } catch (Exception e) {
            logger.error("Error executing scheduled task", e);
            logger.error("Error message: " + e.getMessage());
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
     * Helper method using deprecated Date arithmetic
     */
    private String getTimeUntilNextRun() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        
        // Calculate seconds until next minute
        int secondsIntoMinute = calendar.get(Calendar.SECOND);
        int secondsUntilNextMinute = 60 - secondsIntoMinute;
        
        return secondsUntilNextMinute + " seconds";
    }

    /**
     * Deprecated finalize method (will be removed in future JDK versions)
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            logger.info("MessageScheduledTask is being garbage collected");
        } finally {
            super.finalize();
        }
    }
}
