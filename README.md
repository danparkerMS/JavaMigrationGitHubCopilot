# Java Application Migration Workshop

Welcome to the Java Application Migration Workshop! This hands-on workshop teaches you how to use **GitHub Copilot as an autonomous team member** to modernize legacy Java applications.

## 🎯 Workshop Overview

Learn to migrate a legacy Java application from:
- **JDK 1.8** → **JDK 17** (LTS)
- **Spring Boot 2.7.x** → **Spring Boot 3.x** (includes Spring 6.x)
- **javax.\* packages** → **jakarta.\* packages**
- **Legacy code patterns** → **Modern best practices**
- **Traditional deployment** → **Cloud-native deployment** (Azure Container Apps)

## 📋 Prerequisites

- **GitHub Account** with Copilot access
- **JDK 1.8** installed (for running the legacy app)
- **JDK 17 or higher** installed (for the migrated app)
- **Maven 3.6+** installed
- **Git** installed
- **VS Code** or **IntelliJ IDEA** (recommended)
- **Azure Account** (for deployment steps, optional)

## 🏗️ Legacy Application Architecture

This workshop includes a complete legacy Spring 4.x application with:

### Core Components
- **REST API** (`MessageController`) - CRUD operations for messages
- **Scheduled Task** (`MessageScheduledTask`) - Runs every 60 seconds to report statistics
- **JPA/Hibernate** - Data persistence with H2 in-memory database
- **Spring Boot** - Legacy 2.7.x with intentional outdated patterns

### Technology Stack (Legacy)
| Component | Version | Modern Alternative |
|-----------|---------|-------------------|
| JDK | 1.8 | JDK 17/21 |
| Spring Boot | 2.7.18 | Spring Boot 3.x |
| Spring Framework | 5.3.31 | Spring Framework 6.x |
| Hibernate | 5.6.15 | Hibernate 6.x |
| javax.* packages | javax | jakarta.* (EE 9+) |
| Log4j | 1.2.17 | SLF4J/Logback |
| Commons Lang | 2.6 | Commons Lang 3.x |
| Date/Time API | java.util.Date | java.time.* |

### Migration Challenges

The legacy code intentionally includes patterns that require real migration work:

1. **javax.* → jakarta.*** - Package namespace changes
2. **Date/Calendar → java.time** - Modern date/time API
3. **XML Config → Java Config** - Spring configuration modernization
4. **Field Injection → Constructor Injection** - Best practice improvements
5. **Log4j 1.x → SLF4J/Logback** - Logging framework update
6. **RestTemplate → WebClient/RestClient** - HTTP client modernization
7. **Deprecated APIs** - Remove obsolete constructors and methods
8. **WAR → JAR/Container** - Packaging and deployment changes

## 🚀 Quick Start - Running the Legacy Application

### 1. Clone the Repository

```bash
git clone <repository-url>
cd JavaSample
```

### 2. Verify JDK 1.8

```bash
java -version
# Should show: java version "1.8.0_xxx"
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
- **Home Page**: http://localhost:8080
- **API Endpoints**: http://localhost:8080/api/messages
- **H2 Console**: http://localhost:8080/h2-console

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
```

### 6. Observe Scheduled Task

Watch the console output - every minute you'll see:
```
========================================
Message Statistics Task - Executing
========================================
Execution Time: 2025-11-16 10:23:45
Total Messages: 5
Active Messages: 5
...
```

## 📚 Workshop Steps

Follow the migration workshop in order:

| Step | Title | Duration | Description |
|------|-------|----------|-------------|
| [Step 0](Migration/step-00-introduction.md) | Introduction to GitHub Copilot | 15 min | Learn about Copilot as an autonomous team member |
| [Step 1](Migration/step-01-create-assessment-issue.md) | Create Assessment Issue | 20 min | Have Copilot analyze the legacy application |
| [Step 2](Migration/step-02-review-assessment.md) | Review Assessment | 20 min | Evaluate migration strategies and Azure options |
| [Step 3](Migration/step-03-create-migration-issue.md) | Create Migration Issue | 10 min | Request Copilot to implement the migration |
| [Step 4](Migration/step-04-review-migration.md) | Review Migration Work | 30 min | Examine the migrated code and changes |
| [Step 5](Migration/step-05-local-testing.md) | Local Testing | 30 min | Build and test the modernized application |
| [Step 6](Migration/step-06-deployment.md) | Azure Deployment | 45 min | Deploy to Azure (App Service, Functions, or Containers) |

**Total Time**: 2-3 hours (core workshop) | 3-4 hours (with deployment)

## 🎓 Learning Objectives

By the end of this workshop, you will:

✅ Understand how to use GitHub Copilot as an autonomous team member  
✅ Know how to create effective prompts for code migration  
✅ Be able to assess migration complexity and compare approaches  
✅ Have hands-on experience migrating JDK 1.8 → JDK 17  
✅ Understand Spring Boot 2.7.x → Spring Boot 3.x migration  
✅ Know how to handle javax.* → jakarta.* package changes  
✅ Be familiar with Azure deployment options for Java applications  
✅ Have confidence to apply these techniques to your own projects  

## 🔍 What Makes This Workshop Unique?

Unlike simple dependency updates, this workshop demonstrates:

- **Real migration challenges** - Not just POM changes, but actual code transformations
- **Deprecated API usage** - Learn to modernize Date/Calendar, primitive wrappers, etc.
- **Configuration migration** - XML → Java Config, web.xml → embedded server
- **Cloud-native patterns** - From WAR files to containerized deployments
- **AI-assisted development** - Let Copilot do the heavy lifting while you guide

## 📖 Additional Resources

- [Spring Boot 3.x Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Java 17 Migration Guide](https://docs.oracle.com/en/java/javase/17/migrate/getting-started.html)
- [Jakarta EE Documentation](https://jakarta.ee/specifications/)
- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)

## 🤝 Contributing

Found an issue or want to improve the workshop? Contributions are welcome!

## 📄 License

This workshop is provided as-is for educational purposes.

---

**Ready to start?** Head to [Step 0: Introduction to GitHub Copilot](Migration/step-00-introduction.md) to begin your migration journey! 🚀
