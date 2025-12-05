package com.nytour.demo.service;

import com.nytour.demo.model.Message;
import com.nytour.demo.repository.MessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Message Service - Modern Spring Boot 3.x patterns
 * 
 * MIGRATED FROM:
 * - Field injection → Constructor injection
 * - Calendar/Date APIs → java.time.LocalDateTime
 * - Commons Lang 2.x → Commons Lang 3.x
 */
@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    // Constructor injection (modern pattern)
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(String content, String author) {
        // Using Commons Lang 3.x StringUtils
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
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    public Message updateMessage(Long id, String content) {
        Message message = getMessageById(id);
        message.setContent(content);
        message.setUpdatedDate(LocalDateTime.now());
        
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
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysAgo);
        return messageRepository.findRecentActiveMessages(cutoffDate);
    }

    @Transactional(readOnly = true)
    public Long getActiveMessageCount() {
        return messageRepository.countByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Message> searchMessages(String keyword) {
        String trimmedKeyword = StringUtils.trim(keyword);
        if (StringUtils.isEmpty(trimmedKeyword)) {
            return getAllMessages();
        }
        return messageRepository.findByContentContainingIgnoreCase(trimmedKeyword);
    }
}
