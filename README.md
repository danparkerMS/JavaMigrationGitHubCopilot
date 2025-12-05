# Java Application Migration Workshop

Welcome to the Java Application Migration Workshop! This hands-on workshop teaches you how to use **GitHub Copilot as an autonomous team member** to modernize legacy Java applications.

## 🎯 Current Application Status: **MIGRATED** ✅

This application has been successfully migrated from:
- **JDK 1.8** → **JDK 17** (LTS)
- **Spring Boot 2.7.x** → **Spring Boot 3.x** (includes Spring 6.x)
- **javax.\* packages** → **jakarta.\* packages**
- **Legacy code patterns** → **Modern best practices**
- **Traditional deployment** → **Cloud-native deployment** (Azure Container Apps)

## 📋 Prerequisites

- **JDK 17 or higher** installed
- **Maven 3.6+** installed
- **Git** installed
- **Docker** (for containerized deployment)
- **Azure Account** (for deployment steps, optional)

## 🏗️ Application Architecture

This application includes:

### Core Components
- **REST API** (`MessageController`) - CRUD operations for messages
- **Scheduled Task** (`MessageScheduledTask`) - Runs every 60 seconds to report statistics
- **JPA/Hibernate 6.x** - Data persistence with H2 in-memory database
- **Spring Boot 3.x** - Modern framework with best practices

### Technology Stack
| Component | Version |
|-----------|---------|
| JDK | 17 LTS |
| Spring Boot | 3.2.5 |
| Spring Framework | 6.1.x |
| Hibernate | 6.x |
| Jakarta EE | 10 (jakarta.\*) |
| Logging | SLF4J/Logback |
| Commons Lang | 3.14.0 |
| Date/Time API | java.time.\* |

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd JavaMigrationGitHubCopilot
```

### 2. Verify JDK 17

```bash
java -version
# Should show: openjdk version "17.x.x" or similar
```

### 3. Build and Run

```bash
# Clean and run with Spring Boot
mvn clean spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/message-service.jar
```

### 4. Test the Application

Open your browser to:
- **API Endpoints**: http://localhost:8080/api/messages
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health

### 5. Test API Endpoints

```bash
# Get all messages
curl http://localhost:8080/api/messages

# Create a message
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello Migration Workshop!","author":"developer"}'

# Get message by ID
curl http://localhost:8080/api/messages/1

# Search messages
curl "http://localhost:8080/api/messages/search?keyword=migration"

# Get statistics
curl http://localhost:8080/api/messages/stats
```

### 6. Observe Scheduled Task

Watch the console output - every minute you'll see:
```
========================================
Message Statistics Task - Executing
========================================
Execution Time: 2025-01-01 10:23:45
Total Messages: 5
Active Messages: 5
...
```

## 🐳 Docker Deployment

### Build Docker Image

```bash
docker build -t message-service:latest .
```

### Run Container

```bash
docker run -p 8080:8080 message-service:latest
```

### Test Containerized Application

```bash
curl http://localhost:8080/api/messages
curl http://localhost:8080/actuator/health
```

## ☁️ Azure Deployment

See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed Azure Container Apps deployment instructions.

Quick deployment:
```bash
# Set variables
export RESOURCE_GROUP="message-service-rg"
export APP_NAME="message-service"

# Create and deploy (see DEPLOYMENT.md for full steps)
az containerapp create --name $APP_NAME ...
```

## 📚 Documentation

- [MIGRATION.md](MIGRATION.md) - Detailed summary of all migration changes
- [DEPLOYMENT.md](DEPLOYMENT.md) - Azure Container Apps deployment guide
- [Migration/](Migration/) - Step-by-step workshop instructions

## 📖 Workshop Steps

Follow the migration workshop documentation in the `Migration/` folder:

| Step | Title | Description |
|------|-------|-------------|
| [Step 0](Migration/step-00-introduction.md) | Introduction | Learn about Copilot as an autonomous team member |
| [Step 1](Migration/step-01-create-assessment-issue.md) | Create Assessment | Have Copilot analyze the legacy application |
| [Step 2](Migration/step-02-review-assessment.md) | Review Assessment | Evaluate migration strategies |
| [Step 3](Migration/step-03-create-migration-issue.md) | Create Migration Issue | Request Copilot to implement the migration |
| [Step 4](Migration/step-04-review-migration.md) | Review Migration | Examine the migrated code |
| [Step 5](Migration/step-05-local-testing.md) | Local Testing | Build and test the modernized application |
| [Step 6](Migration/step-06-deployment.md) | Azure Deployment | Deploy to Azure Container Apps |

## 🎓 Key Migration Changes

### Package Namespace
```java
// Before                  // After
javax.persistence.*    →   jakarta.persistence.*
javax.validation.*     →   jakarta.validation.*
```

### Date/Time API
```java
// Before                  // After
java.util.Date         →   java.time.LocalDateTime
SimpleDateFormat       →   DateTimeFormatter
```

### Dependency Injection
```java
// Before (Field Injection)    // After (Constructor Injection)
@Autowired                     private final MessageService service;
private MessageService svc;    public Controller(MessageService svc) {
                                   this.service = svc;
                               }
```

### Logging
```java
// Before (Log4j 1.x)          // After (SLF4J)
Logger.getLogger(...)      →   LoggerFactory.getLogger(...)
logger.info("msg: " + x)   →   logger.info("msg: {}", x)
```

## 🤝 Contributing

Found an issue or want to improve the workshop? Contributions are welcome!

## 📄 License

This workshop is provided as-is for educational purposes.

---

**Application migrated successfully!** 🎉
