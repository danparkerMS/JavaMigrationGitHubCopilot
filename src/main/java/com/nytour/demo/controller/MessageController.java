package com.nytour.demo.controller;

import com.nytour.demo.model.Message;
import com.nytour.demo.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Message Controller - Legacy Spring Boot 2.7.x patterns
 * 
 * MIGRATION CHALLENGES:
 * 1. @Controller + @ResponseBody instead of @RestController
 * 2. Old-style @RequestMapping with method parameter (instead of @GetMapping)
 * 3. Log4j 1.x (deprecated) will migrate to SLF4J
 * 4. SimpleDateFormat (not thread-safe) will migrate to DateTimeFormatter
 * 5. Field injection instead of constructor injection
 * 6. Manual ResponseEntity creation
 * 7. java.util.Date will migrate to java.time.LocalDateTime
 * 8. javax.* packages will migrate to jakarta.* in Spring Boot 3.x
 */
@Controller
@RequestMapping("/api/messages")
public class MessageController {

    // Log4j 1.x (deprecated, migrate to SLF4J)
    private static final Logger logger = Logger.getLogger(MessageController.class);

    // Field injection (legacy pattern)
    @Autowired
    private MessageService messageService;

    // SimpleDateFormat (not thread-safe, deprecated pattern)
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Get all messages - Using @ResponseBody to return JSON
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllMessages(HttpServletRequest request) {
        logger.info("GET /messages - Fetching all messages");
        
        try {
            List<Message> messages = messageService.getAllMessages();
            
            // Manual response building (legacy pattern)
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("status", "success");
            response.put("data", messages);
            response.put("count", messages.size());
            response.put("timestamp", dateFormat.format(new Date())); // Using deprecated Date and SimpleDateFormat
            
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching messages", e);
            return handleError("Failed to fetch messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get message by ID
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMessageById(
            @PathVariable("id") Long id,
            HttpServletResponse response) {
        
        logger.info("GET /messages/" + id);
        
        try {
            Message message = messageService.getMessageById(id);
            
            Map<String, Object> responseMap = new HashMap<String, Object>();
            responseMap.put("status", "success");
            responseMap.put("data", message);
            
            return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Message not found: " + id, e);
            return handleError("Message not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create new message - Using @Valid with javax.validation
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createMessage(
            @Valid @RequestBody CreateMessageRequest request) {
        
        logger.info("POST /messages - Creating message from author: " + request.getAuthor());
        
        try {
            Message message = messageService.createMessage(request.getContent(), request.getAuthor());
            
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("status", "success");
            response.put("message", "Message created successfully");
            response.put("data", message);
            response.put("createdAt", dateFormat.format(new Date()));
            
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid message data", e);
            return handleError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateMessage(
            @PathVariable("id") Long id,
            @RequestBody UpdateMessageRequest request) {
        
        logger.info("PUT /messages/" + id);
        
        try {
            Message message = messageService.updateMessage(id, request.getContent());
            
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("status", "success");
            response.put("message", "Message updated successfully");
            response.put("data", message);
            
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error updating message", e);
            return handleError("Failed to update message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMessage(@PathVariable("id") Long id) {
        logger.info("DELETE /messages/" + id);
        
        try {
            messageService.deleteMessage(id);
            
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("status", "success");
            response.put("message", "Message deleted successfully");
            
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error deleting message", e);
            return handleError("Failed to delete message", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Search messages by keyword
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMessages(
            @RequestParam(value = "keyword", required = false) String keyword) {
        
        logger.info("GET /messages/search?keyword=" + keyword);
        
        List<Message> messages = messageService.searchMessages(keyword);
        
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", "success");
        response.put("data", messages);
        response.put("count", messages.size());
        
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    /**
     * Get messages by author
     */
    @RequestMapping(value = "/author/{author}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMessagesByAuthor(@PathVariable("author") String author) {
        logger.info("GET /messages/author/" + author);
        
        List<Message> messages = messageService.getMessagesByAuthor(author);
        
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", "success");
        response.put("data", messages);
        response.put("author", author);
        
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    /**
     * Get statistics - Using ModelAndView (legacy pattern)
     */
    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public ModelAndView getStatistics() {
        logger.info("GET /messages/stats");
        
        ModelAndView mav = new ModelAndView("stats"); // Returns a view name
        mav.addObject("totalMessages", messageService.getAllMessages().size());
        mav.addObject("activeMessages", messageService.getActiveMessageCount());
        mav.addObject("timestamp", dateFormat.format(new Date()));
        
        return mav;
    }

    // Helper method for error responses
    private ResponseEntity<Map<String, Object>> handleError(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<String, Object>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        errorResponse.put("timestamp", dateFormat.format(new Date()));
        
        return new ResponseEntity<Map<String, Object>>(errorResponse, status);
    }

    // Inner classes for request DTOs (legacy pattern, could be separate files)
    
    public static class CreateMessageRequest {
        @javax.validation.constraints.NotNull
        @javax.validation.constraints.Size(min = 1, max = 500)
        private String content;
        
        @javax.validation.constraints.NotNull
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
        @javax.validation.constraints.NotNull
        @javax.validation.constraints.Size(min = 1, max = 500)
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
