# Step 3: Create Migration Issue

**Duration**: 10 minutes (+ Copilot processing time: 15-30 minutes)

## 🎯 Objectives

- Create an issue requesting Copilot to implement the migration
- Provide clear instructions and requirements
- Reference the assessment from Step 2
- Specify the chosen Azure deployment approach

## 📋 Prerequisites

- [ ] Completed Step 2: Review Assessment
- [ ] Chosen Azure deployment approach
- [ ] Understanding of migration requirements
- [ ] Assessment issue number handy for reference

## 🚀 Task Overview

In this step, Copilot will:
1. Implement the migration across all files
2. Update dependencies and configurations
3. Modernize deprecated APIs
4. Create deployment documentation
5. Generate a Pull Request with all changes

**This is where the magic happens!** 🎩✨

## 📝 Creating the Migration Issue

### Step 3.1: Create New Issue

1. Navigate to your repository
2. Click **"Issues"** tab
3. Click **"New Issue"**

### Step 3.2: Title and Description

**Title**: `Implement Migration - JDK 1.8 to JDK 17 and Spring 4.x to Spring Boot 3.x`

**Description Template**:

```markdown
@copilot Please implement the migration for this Java application based on the assessment in issue #[ASSESSMENT_ISSUE_NUMBER].

## Migration Summary

Based on the assessment, implement a complete migration from:

**Current State**:
- JDK 1.8
- Spring Boot 2.7.18 (last 2.x version)
- javax.* packages
- Hibernate 4.x
- Log4j 1.x
- WAR deployment

**Target State**:
- JDK 17 LTS
- Spring Boot 3.x (with Spring Framework 6.x)
- jakarta.* packages
- Hibernate 6.x (via Spring Boot)
- SLF4J + Logback
- Executable JAR

## Implementation Requirements

### 1. Core Migration Tasks

#### Dependencies (pom.xml)
- [ ] Update Maven compiler source/target to Java 17
- [ ] Migrate to Spring Boot 3.x parent POM
- [ ] Add Spring Boot Starter dependencies
- [ ] Update Hibernate version (managed by Spring Boot)
- [ ] Replace Log4j with SLF4J/Logback
- [ ] Update Commons Lang to 3.x
- [ ] Remove servlet API dependency (provided by Spring Boot)
- [ ] Update Jackson version
- [ ] Add Spring Boot Maven plugin

#### Package Migrations
- [ ] javax.persistence.* → jakarta.persistence.*
- [ ] javax.validation.* → jakarta.validation.*
- [ ] javax.servlet.* → jakarta.servlet.*
- [ ] javax.transaction.* → jakarta.transaction.*

#### Code Modernization
- [ ] java.util.Date → java.time.LocalDateTime
- [ ] java.util.Calendar → java.time APIs
- [ ] SimpleDateFormat → DateTimeFormatter
- [ ] Field injection → Constructor injection
- [ ] @Controller + @ResponseBody → @RestController
- [ ] Remove deprecated primitive wrapper constructors
- [ ] Remove finalize() methods
- [ ] Update Hibernate-specific annotations

#### Configuration Migration
- [ ] Remove web.xml
- [ ] Remove applicationContext.xml
- [ ] Remove dispatcher-servlet.xml
- [ ] Create Spring Boot application class with @SpringBootApplication
- [ ] Create application.properties or application.yml
- [ ] Configure H2 database in Spring Boot style
- [ ] Enable scheduling with @EnableScheduling

#### Logging Migration
- [ ] Remove log4j.properties
- [ ] Update Logger imports to SLF4J
- [ ] Create logback-spring.xml (or use Spring Boot defaults)
- [ ] Update all log statements if needed

### 2. Azure Deployment - Container Apps Approach

**Selected Approach**: Azure Container Apps

Please implement:

#### Dockerfile
- [ ] Create Dockerfile for the Spring Boot application
- [ ] Use appropriate Java 17 base image
- [ ] Copy JAR file and configure entrypoint
- [ ] Expose port 8080
- [ ] Follow Docker best practices

#### Azure Configuration
- [ ] Create deployment documentation for Azure Container Apps
- [ ] Include steps for creating Container Apps environment
- [ ] Document how to deploy the container
- [ ] Explain how scheduled task works in Container Apps
- [ ] Provide CLI commands or ARM template

#### Environment Configuration
- [ ] Document required environment variables
- [ ] Configure H2 console access if needed
- [ ] Set up health check endpoints

### 3. Critical Requirements

⚠️ **MUST PRESERVE FUNCTIONALITY**:

1. **Scheduled Task**
   - MUST run every 60 seconds (every minute)
   - MUST log message statistics
   - MUST use Spring Boot's @Scheduled with same timing
   - Verify this works in containerized environment

2. **REST API**
   - All CRUD endpoints must work identically
   - Response format should remain consistent
   - HTTP status codes preserved

3. **Database**
   - H2 in-memory database
   - Sample data must be loaded
   - Entity mappings preserved

### 4. Testing & Documentation

Please provide:

#### Local Testing Instructions
- [ ] How to build the application (`mvn clean package`)
- [ ] How to run locally (`java -jar` or `mvn spring-boot:run`)
- [ ] How to test endpoints (curl examples)
- [ ] How to verify scheduled task is running

#### Docker Testing
- [ ] How to build Docker image locally
- [ ] How to run container locally
- [ ] How to test in container environment

#### Azure Deployment Guide
- [ ] Step-by-step Azure Container Apps deployment
- [ ] Required Azure CLI commands
- [ ] Configuration steps
- [ ] How to monitor/troubleshoot
- [ ] How to view logs

#### Migration Summary Document
- [ ] List of files changed
- [ ] Key code transformations performed
- [ ] Configuration changes
- [ ] Breaking changes to be aware of
- [ ] Testing checklist

### 5. Code Quality

Please ensure:
- ✅ Follow Spring Boot 3.x best practices
- ✅ Use constructor injection consistently
- ✅ Apply modern Java idioms (var, enhanced switch, etc.)
- ✅ Include proper exception handling
- ✅ Add appropriate validation
- ✅ Use Optional where applicable
- ✅ Maintain or improve code comments

## Deliverables

Create a Pull Request with:

1. **All migrated code files**
   - Updated Java classes
   - New Spring Boot application class
   - Migrated configuration

2. **Build configuration**
   - Updated pom.xml
   - Removed legacy files

3. **Docker configuration**
   - Dockerfile
   - .dockerignore (if needed)

4. **Documentation**
   - MIGRATION.md (summary of changes)
   - DEPLOYMENT.md (Azure deployment guide)
   - Updated README.md

5. **Testing artifacts**
   - Example requests
   - Testing checklist

## Pull Request Description Template

Please structure the PR description as:

```markdown
## Migration Summary
- JDK 1.8 → JDK 17
- Spring 4.x → Spring Boot 3.x
- javax.* → jakarta.*
- [Other key changes]

## Files Changed
- [List major files and their changes]

## How to Test
1. [Build instructions]
2. [Run instructions]
3. [Test instructions]

## Deployment
- [Link to DEPLOYMENT.md]

## Breaking Changes
- [List any breaking changes]

## Checklist
- [ ] All tests pass
- [ ] Application starts successfully
- [ ] API endpoints work
- [ ] Scheduled task runs every minute
- [ ] Docker image builds
- [ ] Documentation complete
```

## Additional Context

- This is for a migration workshop, so code clarity is important
- Comments explaining "why" certain changes were made are valuable
- We want to showcase the power of Spring Boot's autoconfiguration
- The goal is educational as well as functional

## Questions?

If you need any clarification on requirements or approach, please ask before starting implementation.

Thank you! 🚀
```

### Step 3.3: Customize the Template

Replace `[ASSESSMENT_ISSUE_NUMBER]` with your actual assessment issue number.

**Example**: If your assessment was issue #1, write:
```markdown
based on the assessment in issue #1.
```

### Step 3.4: Review and Submit

1. Read through the entire issue
2. Ensure all requirements are clear
3. Verify the chosen Azure approach is correct
4. Click **"Submit new issue"**

## ⏳ What Happens Next

### Copilot's Process

1. **Analyze Repository** (2-5 min)
   - Scan all code files
   - Identify patterns and dependencies
   - Plan transformation strategy

2. **Implement Changes** (10-20 min)
   - Update all Java files
   - Modify build configuration
   - Create new configuration files
   - Generate Docker setup
   - Write documentation

3. **Create Pull Request** (2-5 min)
   - Commit all changes to a new branch
   - Generate comprehensive PR description
   - Link back to the issue

**Total Time**: Typically 15-30 minutes

### Progress Indicators

Watch for:
- Copilot comment: "I'm working on this..."
- Activity on the issue
- New branch created in repository
- Pull request appears

## 💡 While You Wait

Use this time to:

1. **Review assessment again** - Refresh your understanding
2. **Prepare local environment**:
   ```powershell
   # Verify JDK 17 is installed
   java -version
   
   # Verify Maven
   mvn -version
   
   # Verify Docker (if using Container Apps)
   docker --version
   ```

3. **Read Spring Boot docs**:
   - [Spring Boot 3.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes)
   - [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)

4. **Review Azure Container Apps docs**:
   - [Azure Container Apps Overview](https://learn.microsoft.com/en-us/azure/container-apps/overview)
   - [Deploy to Container Apps](https://learn.microsoft.com/en-us/azure/container-apps/quickstart-portal)

## 🔍 Monitoring Progress

### Check Issue Activity

Copilot may post updates like:

```markdown
I'm working on implementing this migration. I'll create a pull request with the changes shortly.
```

### Look for New Branches

Check your repository's branches:
```powershell
git fetch
git branch -r
```

Look for branches like:
- `copilot-migration-jdk17-springboot3`
- `copilot/migration-implementation`

### Watch for PR

You'll get a notification when the PR is created.

## ⚠️ Troubleshooting

### Issue: No Response After 30 Minutes

**Try**:
1. Add a comment: `@copilot Status update please?`
2. Check Copilot service status
3. Create a new, simpler issue
4. Contact GitHub support if needed

### Issue: Response Says "I Can't Do This"

**Possible reasons**:
- Request too complex/broad
- Repository too large
- Technical limitations

**Solution**:
```markdown
@copilot Let's break this down. Please start with:

1. Update pom.xml to Spring Boot 3.x
2. Create Spring Boot application class
3. Migrate one controller as an example

We'll do the rest iteratively.
```

### Issue: Copilot Asks Questions

**Example**:
```markdown
Which specific Azure Container Apps tier should I target - Consumption or Dedicated?
```

**Response**:
```markdown
@copilot Please target the Consumption tier for Azure Container Apps, as it's cost-effective for this workshop scenario.
```

## 📋 Expected PR Structure

When Copilot creates the PR, expect:

### Changed Files
```
Modified:
  - pom.xml
  - src/main/java/.../Message.java
  - src/main/java/.../MessageController.java
  - src/main/java/.../MessageService.java
  - src/main/java/.../MessageRepository.java
  - src/main/java/.../MessageScheduledTask.java
  - src/main/resources/application.properties

Added:
  - src/main/java/.../Application.java
  - Dockerfile
  - .dockerignore
  - MIGRATION.md
  - DEPLOYMENT.md

Removed:
  - src/main/webapp/WEB-INF/web.xml
  - src/main/webapp/WEB-INF/applicationContext.xml
  - src/main/webapp/WEB-INF/dispatcher-servlet.xml
  - src/main/resources/log4j.properties
```

### PR Size

Typical stats:
- **Files changed**: 15-25
- **Lines added**: 300-600
- **Lines removed**: 200-400

## ✅ Checklist - Step 3 Complete

Before moving to Step 4:

- [ ] Migration issue created with clear requirements
- [ ] Referenced assessment issue for context
- [ ] Specified chosen Azure approach (Container Apps)
- [ ] Listed all required migration tasks
- [ ] Emphasized critical requirements (scheduled task)
- [ ] Requested comprehensive documentation
- [ ] Copilot has created a Pull Request
- [ ] PR includes code changes and documentation

## 🎓 Key Takeaways

1. **Be Comprehensive** - List all requirements clearly
2. **Reference Context** - Link to assessment for consistency
3. **Emphasize Criticality** - Highlight non-negotiable requirements
4. **Request Documentation** - Code alone isn't enough
5. **Be Patient** - Quality migration takes time
6. **Be Ready to Iterate** - May need follow-up questions

---

## 🎯 Next Step

Once the Pull Request is created, you're ready to review the implementation!

**→ Continue to [Step 4: Review Migration Work](step-04-review-migration.md)**
