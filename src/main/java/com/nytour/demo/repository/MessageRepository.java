package com.nytour.demo.repository;

import com.nytour.demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Message Repository - Spring Data JPA 1.x (legacy)
 * 
 * MIGRATION CHALLENGES:
 * 1. Spring Data JPA 1.x to 3.x API changes
 * 2. Date parameters will change to LocalDateTime
 * 3. Query method naming conventions may have updates
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find by author (standard Spring Data JPA)
    List<Message> findByAuthor(String author);

    // Find active messages (legacy boolean handling)
    List<Message> findByActiveTrue();

    // Find by date range using deprecated Date API
    List<Message> findByCreatedDateBetween(Date startDate, Date endDate);

    // Custom JPQL query with Date parameter
    @Query("SELECT m FROM Message m WHERE m.createdDate > :date AND m.active = true")
    List<Message> findRecentActiveMessages(@Param("date") Date date);

    // Count active messages
    Long countByActiveTrue();

    // Find by content containing (case-insensitive search)
    List<Message> findByContentContainingIgnoreCase(String keyword);

    // Delete by author (Spring Data JPA 1.x style)
    void deleteByAuthor(String author);
}
