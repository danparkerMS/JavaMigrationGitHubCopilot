package com.nytour.demo.repository;

import com.nytour.demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Message Repository - Modern Spring Data JPA 3.x
 * 
 * MIGRATED FROM:
 * - java.util.Date parameters → java.time.LocalDateTime
 * - Spring Data JPA 1.x → Spring Data JPA 3.x
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find by author (standard Spring Data JPA)
    List<Message> findByAuthor(String author);

    // Find active messages
    List<Message> findByActiveTrue();

    // Find by date range using LocalDateTime
    List<Message> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Custom JPQL query with LocalDateTime parameter
    @Query("SELECT m FROM Message m WHERE m.createdDate > :date AND m.active = true")
    List<Message> findRecentActiveMessages(@Param("date") LocalDateTime date);

    // Count active messages
    Long countByActiveTrue();

    // Find by content containing (case-insensitive search)
    List<Message> findByContentContainingIgnoreCase(String keyword);

    // Delete by author
    void deleteByAuthor(String author);
}
