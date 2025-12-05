-- H2 Database initialization script
-- This will be executed automatically on application startup

-- Insert sample messages (IDs auto-generated)
INSERT INTO messages (content, author, created_date, updated_date, is_active) 
VALUES ('Welcome to the Message Service!', 'admin', CURRENT_TIMESTAMP, NULL, TRUE);

INSERT INTO messages (content, author, created_date, updated_date, is_active) 
VALUES ('This is a modern Spring Boot 3.x application running on JDK 17', 'system', CURRENT_TIMESTAMP, NULL, TRUE);

INSERT INTO messages (content, author, created_date, updated_date, is_active) 
VALUES ('Successfully migrated from Spring Boot 2.7 to Spring Boot 3.x!', 'admin', CURRENT_TIMESTAMP, NULL, TRUE);

INSERT INTO messages (content, author, created_date, updated_date, is_active) 
VALUES ('Using H2 in-memory database for easy testing', 'system', CURRENT_TIMESTAMP, NULL, TRUE);

INSERT INTO messages (content, author, created_date, updated_date, is_active) 
VALUES ('Now using jakarta.* packages and modern java.time APIs', 'developer', CURRENT_TIMESTAMP, NULL, TRUE);
