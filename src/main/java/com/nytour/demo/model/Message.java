package com.nytour.demo.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Message Entity - Legacy JPA 2.1 with javax.persistence
 * 
 * MIGRATION CHALLENGES:
 * 1. javax.persistence.* will migrate to jakarta.persistence.*
 * 2. java.util.Date (deprecated) will migrate to java.time.LocalDateTime
 * 3. @Type annotation is Hibernate-specific and may need updates
 * 4. Primitive wrapper constructors (new Long()) are deprecated in Java 9+
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Content cannot be null")
    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    @Column(nullable = false, length = 500)
    private String content;

    @NotNull
    @Column(name = "author")
    private String author;

    // Using deprecated java.util.Date instead of java.time.LocalDateTime
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "is_active")
    @Type(type = "yes_no") // Hibernate 4.x specific type annotation
    private Boolean active;

    // Default constructor required by JPA
    public Message() {
        // Initialize with deprecated Date constructor
        this.createdDate = new Date();
        this.active = Boolean.TRUE; // Using deprecated Boolean constructor pattern
    }

    // Constructor with deprecated Date API
    public Message(String content, String author) {
        this.content = content;
        this.author = author;
        this.createdDate = new Date(); // Will migrate to LocalDateTime.now()
        this.active = new Boolean(true); // Deprecated constructor
    }

    // PrePersist callback using deprecated Date
    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = new Date();
        }
    }

    // PreUpdate callback using deprecated Date
    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", createdDate=" + createdDate +
                ", active=" + active +
                '}';
    }
}
