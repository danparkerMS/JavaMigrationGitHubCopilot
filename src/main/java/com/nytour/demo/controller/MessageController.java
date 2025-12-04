package com.nytour.demo.controller;

import com.nytour.demo.model.Message;
import com.nytour.demo.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Message Controller - Modern Spring Boot 3.x patterns
 * 
 * MIGRATED FROM:
 * - @Controller + @ResponseBody → @RestController
 * - Old-style @RequestMapping → @GetMapping, @PostMapping, etc.
 * - Log4j 1.x → SLF4J
 * - SimpleDateFormat → DateTimeFormatter
 * - Field injection → Constructor injection
 * - javax.* → jakarta.*
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;
    
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructor injection (modern pattern)
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Get all messages
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMessages() {
        logger.info("GET /messages - Fetching all messages");
        
        try {
            List<Message> messages = messageService.getAllMessages();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", messages);
            response.put("count", messages.size());
            response.put("timestamp", LocalDateTime.now().format(dateTimeFormatter));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching messages", e);
            return handleError("Failed to fetch messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get message by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMessageById(@PathVariable Long id) {
        logger.info("GET /messages/{}", id);
        
        try {
            Message message = messageService.getMessageById(id);
            
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "success");
            responseMap.put("data", message);
            
            return ResponseEntity.ok(responseMap);
        } catch (RuntimeException e) {
            logger.error("Message not found: {}", id, e);
            return handleError("Message not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create new message
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMessage(@Valid @RequestBody CreateMessageRequest request) {
        logger.info("POST /messages - Creating message from author: {}", request.getAuthor());
        
        try {
            Message message = messageService.createMessage(request.getContent(), request.getAuthor());
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message created successfully");
            response.put("data", message);
            response.put("createdAt", LocalDateTime.now().format(dateTimeFormatter));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid message data", e);
            return handleError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update message
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMessage(
            @PathVariable Long id,
            @RequestBody UpdateMessageRequest request) {
        
        logger.info("PUT /messages/{}", id);
        
        try {
            Message message = messageService.updateMessage(id, request.getContent());
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message updated successfully");
            response.put("data", message);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error updating message", e);
            return handleError("Failed to update message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMessage(@PathVariable Long id) {
        logger.info("DELETE /messages/{}", id);
        
        try {
            messageService.deleteMessage(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error deleting message", e);
            return handleError("Failed to delete message", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Search messages by keyword
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMessages(
            @RequestParam(value = "keyword", required = false) String keyword) {
        
        logger.info("GET /messages/search?keyword={}", keyword);
        
        List<Message> messages = messageService.searchMessages(keyword);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", messages);
        response.put("count", messages.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get messages by author
     */
    @GetMapping("/author/{author}")
    public ResponseEntity<Map<String, Object>> getMessagesByAuthor(@PathVariable String author) {
        logger.info("GET /messages/author/{}", author);
        
        List<Message> messages = messageService.getMessagesByAuthor(author);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", messages);
        response.put("author", author);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get statistics as JSON (modernized from ModelAndView)
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        logger.info("GET /messages/stats");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMessages", messageService.getAllMessages().size());
        stats.put("activeMessages", messageService.getActiveMessageCount());
        stats.put("timestamp", LocalDateTime.now().format(dateTimeFormatter));
        
        return ResponseEntity.ok(stats);
    }

    // Helper method for error responses
    private ResponseEntity<Map<String, Object>> handleError(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now().format(dateTimeFormatter));
        
        return ResponseEntity.status(status).body(errorResponse);
    }

    // Inner classes for request DTOs with jakarta.validation
    
    public static class CreateMessageRequest {
        @NotNull
        @Size(min = 1, max = 500)
        private String content;
        
        @NotNull
        private String author;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

    public static class UpdateMessageRequest {
        @NotNull
        @Size(min = 1, max = 500)
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
