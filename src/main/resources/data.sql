-- H2 Database initialization script
-- This will be executed automatically on application startup

-- Insert sample messages
INSERT INTO messages (id, content, author, created_date, updated_date, is_active) 
VALUES (1, 'Welcome to the Message Service!', 'admin', CURRENT_TIMESTAMP, NULL, 'Y');

INSERT INTO messages (id, content, author, created_date, updated_date, is_active) 
VALUES (2, 'This is a legacy Spring 4.x application running on JDK 1.8', 'system', CURRENT_TIMESTAMP, NULL, 'Y');

INSERT INTO messages (id, content, author, created_date, updated_date, is_active) 
VALUES (3, 'Ready for migration to modern Spring Boot 3.x and JDK 17!', 'admin', CURRENT_TIMESTAMP, NULL, 'Y');

INSERT INTO messages (id, content, author, created_date, updated_date, is_active) 
VALUES (4, 'Using H2 in-memory database for easy testing', 'system', CURRENT_TIMESTAMP, NULL, 'Y');

INSERT INTO messages (id, content, author, created_date, updated_date, is_active) 
VALUES (5, 'Legacy code includes javax.* packages and deprecated Date APIs', 'developer', CURRENT_TIMESTAMP, NULL, 'Y');
