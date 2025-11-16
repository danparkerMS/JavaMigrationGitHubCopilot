package com.nytour.demo.service;

import com.nytour.demo.model.Message;
import com.nytour.demo.repository.MessageRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Message Service - Legacy Spring 4.x patterns
 * 
 * MIGRATION CHALLENGES:
 * 1. Field injection (@Autowired on fields) is discouraged in modern Spring
 * 2. Calendar and Date APIs are deprecated (use java.time)
 * 3. Commons Lang 2.x (deprecated, migrate to Commons Lang 3.x)
 * 4. Manual null checking instead of Optional
 */
@Service
@Transactional
public class MessageService {

    // Field injection (legacy pattern, constructor injection preferred in modern Spring)
    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(String content, String author) {
        // Using deprecated commons-lang StringUtils (version 2.x)
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(author)) {
            throw new IllegalArgumentException("Content and author cannot be empty");
        }

        Message message = new Message(content, author);
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Message getMessageById(Long id) {
        // Legacy pattern: direct get without Optional handling
        Message message = messageRepository.findById(id).orElse(null); // Using findById for Spring Boot 2.x
        if (message == null) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        return message;
    }

    public Message updateMessage(Long id, String content) {
        Message message = getMessageById(id);
        message.setContent(content);
        
        // Using deprecated Date and Calendar APIs
        Calendar calendar = Calendar.getInstance();
        message.setUpdatedDate(calendar.getTime());
        
        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        Message message = getMessageById(id);
        messageRepository.delete(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getMessagesByAuthor(String author) {
        return messageRepository.findByAuthor(author);
    }

    @Transactional(readOnly = true)
    public List<Message> getRecentMessages(int daysAgo) {
        // Using deprecated Calendar API to calculate date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -daysAgo);
        Date cutoffDate = calendar.getTime();
        
        return messageRepository.findRecentActiveMessages(cutoffDate);
    }

    @Transactional(readOnly = true)
    public Long getActiveMessageCount() {
        return messageRepository.countByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Message> searchMessages(String keyword) {
        // Trim using commons-lang 2.x
        String trimmedKeyword = StringUtils.trim(keyword);
        if (StringUtils.isEmpty(trimmedKeyword)) {
            return getAllMessages();
        }
        return messageRepository.findByContentContainingIgnoreCase(trimmedKeyword);
    }
}
