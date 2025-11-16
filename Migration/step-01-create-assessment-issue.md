# Step 1: Create Assessment Issue

**Duration**: 20 minutes

## 🎯 Objectives

- Create a GitHub Issue to request application assessment
- Use effective prompts to get comprehensive analysis
- Understand what information Copilot needs

## 📋 Prerequisites

- [ ] Completed Step 0: Introduction
- [ ] Legacy application is pushed to a GitHub repository
- [ ] Copilot access verified
- [ ] Understanding of current application architecture

## 🚀 Task Overview

In this step, you'll create a GitHub Issue asking Copilot to:
1. Analyze the legacy Java application
2. Identify migration challenges
3. Recommend modernization strategies
4. Compare Azure deployment options
5. Provide effort estimates

## 📝 Creating the Assessment Issue

### Step 1.1: Navigate to Your Repository

1. Go to your GitHub repository containing the legacy application
2. Click on the **"Issues"** tab
3. Click **"New Issue"**

### Step 1.2: Create the Issue

**Title**: `Application Migration Assessment - Spring Boot 2.7.x to Spring Boot 3.x`

**Description**: Use the template below (customize as needed)

```markdown
@copilot Please provide a comprehensive assessment for migrating this legacy Java application to modern technologies.

## Current State

### Technology Stack
- **JDK**: 1.8 (released 2014, compiled with 1.8 target)
- **Spring Boot**: 2.7.18 (last 2.x version, released 2023)
- **Spring Framework**: 5.3.31 (bundled with Spring Boot 2.7)
- **Hibernate**: 5.6.15.Final (bundled with Spring Boot 2.7)
- **Build Tool**: Maven
- **Packaging**: JAR (executable with embedded Tomcat)
- **Database**: H2 in-memory
- **Logging**: Log4j 1.2.17 (deprecated, should use SLF4J)

### Application Components
1. **REST API** (`MessageController`)
   - CRUD operations for messages
   - Endpoints: GET, POST, PUT, DELETE at `/api/messages`
   - Uses `javax.servlet.*` and `javax.validation.*` packages
   - Legacy `@Controller` with `@ResponseBody` (should use `@RestController`)

2. **Scheduled Task** (`MessageScheduledTask`)
   - **CRITICAL**: Must run every 60 seconds (every minute)
   - Reports message statistics
   - Uses `@Scheduled(fixedDelay = 60000)`

3. **Data Layer**
   - JPA 2.2 with `javax.persistence.*`
   - Spring Data JPA 2.x
   - H2 in-memory database
   - Hibernate 5.6.x as ORM

4. **Configuration**
   - Spring Boot 2.7.x with `application.properties`
   - `@SpringBootApplication` main class
   - Auto-configuration enabled

### Known Legacy Patterns
- Field injection instead of constructor injection
- `java.util.Date` and `Calendar` instead of `java.time`
- `SimpleDateFormat` (not thread-safe)
- Log4j 1.x (deprecated)
- Commons Lang 2.x (deprecated)
- Deprecated primitive wrapper constructors (`new Boolean()`, `new Integer()`)
- `finalize()` methods
- `@Controller` + `@ResponseBody` instead of `@RestController`
- Old-style `@RequestMapping` with `method` parameter

## Migration Goals

### Target State
- **JDK**: 17 LTS (or 21 LTS)
- **Spring Boot**: 3.x (latest stable, includes Spring Framework 6.x)
- **Packages**: jakarta.* namespace (Jakarta EE 9+)
- **Deployment**: Azure Container Apps
- **Logging**: SLF4J with Logback (Spring Boot default)
- **Best Practices**: Constructor injection, modern APIs, @RestController

### Requirements
1. ✅ Preserve all existing functionality
2. ✅ Scheduled task MUST run every minute (critical business requirement)
3. ✅ Maintain H2 in-memory database (for workshop simplicity)
4. ✅ Deploy to Azure cloud
5. ✅ Follow Spring Boot 3.x best practices
6. ✅ Update to modern Java APIs (java.time, etc.)
7. ✅ Containerize for cloud deployment (Docker)

## Assessment Requests

Please provide:

### 1. Migration Analysis
- List all breaking changes from Spring Boot 2.7 → 3.x
- Identify JDK 1.8 → 17 compatibility issues
- Document javax.* → jakarta.* namespace changes required
- Highlight deprecated API usage that must be updated
- Analyze impact of Spring Framework 5.x → 6.x changes

### 2. Azure Deployment Options
Compare at least **3 different Azure deployment approaches**:

For each option, provide:
- Azure service(s) used
- How the REST API would be hosted
- How the scheduled task would be implemented
- Pros and cons
- Cost considerations (relative: $, $$, $$$)
- Complexity level (Simple, Moderate, Complex)
- Best use case

Example options to consider:
- Azure App Service + Azure Functions (Timer)
- Azure Container Apps
- Azure Spring Apps
- Azure Kubernetes Service (AKS)
- All-in Azure Functions (HTTP + Timer)

### 3. Recommended Approach
Based on the analysis:
- Which Azure option do you recommend and why?
- What is the migration effort (hours/days)?
- What are the highest risk areas?
- Suggested migration order/phases

### 4. Code Change Examples
Provide before/after examples for:
- Dependency changes (`pom.xml` - Spring Boot 2.7 → 3.x parent)
- Package imports (javax → jakarta)
- Date API usage (Date → LocalDateTime)
- Controller patterns (@Controller + @ResponseBody → @RestController)
- RequestMapping (@RequestMapping(method=...) → @GetMapping/@PostMapping)
- Logging (Log4j → SLF4J)

## Deliverables

Please include in your response:
- [ ] Detailed migration analysis
- [ ] Comparison table of Azure options
- [ ] Recommended approach with justification
- [ ] Estimated effort and timeline
- [ ] Risk assessment
- [ ] Code examples for key changes

## Context

This is part of a migration workshop to teach developers how to modernize legacy Java applications using GitHub Copilot. The assessment should be thorough and educational.
```

### Step 1.3: Submit the Issue

1. Review your issue for completeness
2. Click **"Submit new issue"**
3. Wait for Copilot to respond (typically 5-15 minutes)

## 💡 Understanding the Prompt Structure

Let's break down why this prompt is effective:

### Section 1: Current State
```markdown
### Technology Stack
- **JDK**: 1.8 (released 2014)
- **Spring Framework**: 4.3.30.RELEASE (legacy, not Spring Boot)
...
```

**Why**: Provides complete context about where we're starting from. Copilot needs this to understand the gap between current and target state.

### Section 2: Application Components
```markdown
2. **Scheduled Task** (`MessageScheduledTask`)
   - **CRITICAL**: Must run every 60 seconds (every minute)
```

**Why**: Highlights critical business requirements. The "CRITICAL" marker ensures Copilot prioritizes this in its recommendations.

### Section 3: Known Legacy Patterns
```markdown
- Field injection instead of constructor injection
- `java.util.Date` and `Calendar` instead of `java.time`
```

**Why**: Explicitly lists technical debt. This helps Copilot identify areas that need modernization beyond just version upgrades.

### Section 4: Assessment Requests
```markdown
Compare at least **3 different Azure deployment approaches**:

For each option, provide:
- Azure service(s) used
- Pros and cons
...
```

**Why**: Specific, structured requests get better responses. We're asking for comparison, not just one option.

### Section 5: Deliverables
```markdown
Please include in your response:
- [ ] Detailed migration analysis
- [ ] Comparison table of Azure options
```

**Why**: Checklists ensure completeness. Copilot will try to address each item.

## 🎯 What to Expect

After submitting, Copilot will:

1. **Analyze the codebase** - Scan all files in the repository
2. **Identify patterns** - Detect legacy code patterns automatically
3. **Research options** - Compare different migration approaches
4. **Generate response** - Comprehensive assessment with recommendations

**Response time**: Typically 5-15 minutes

**Response format**: Copilot will comment on your issue with:
- Markdown-formatted analysis
- Tables comparing options
- Code examples
- Recommendations

## 📊 Expected Response Content

Copilot's response should include:

### Migration Challenges
- JDK 1.8 → 17 breaking changes
- Spring 4.x → Boot 3.x API changes
- javax.* → jakarta.* remapping
- Deprecated API replacements

### Azure Options Comparison

Example table format:

| Option | Services | API Hosting | Scheduled Task | Complexity | Cost | Pros | Cons |
|--------|----------|-------------|----------------|------------|------|------|------|
| App Service + Functions | 2 services | App Service | Function Timer | Moderate | $$ | Managed | 2 services |
| Container Apps | 1 service | Container Apps | Scheduled Job | Simple | $ | Modern | Learning curve |
| ... | ... | ... | ... | ... | ... | ... | ... |

### Recommendation
- Preferred approach with justification
- Effort estimate
- Risk areas
- Migration phases

### Code Examples
- Before/after snippets
- Key file changes
- Configuration updates

## ⚠️ Common Issues

### Issue: Copilot doesn't respond

**Solutions**:
- Wait up to 30 minutes
- Check that you used `@copilot` in the issue
- Verify Copilot access is enabled
- Try creating a new issue with simplified prompt

### Issue: Response is too generic

**Solutions**:
- Add more specific requirements to the issue
- Reference specific files or code patterns
- Ask follow-up questions in comments
- Request more detail on specific areas

### Issue: Missing Azure deployment options

**Solutions**:
- Comment on the issue: "@copilot Please expand on Azure deployment options"
- Be specific: "Compare Azure Container Apps vs App Service for this scenario"

## 🔄 Iterating on the Assessment

You can ask follow-up questions in issue comments:

```markdown
@copilot Thanks for the assessment! A few follow-up questions:

1. Can you provide more detail on the javax → jakarta package mapping?
2. What are the risks of using Azure Container Apps vs App Service?
3. How would authentication/authorization work in each Azure option?
```

## ✅ Checklist - Step 1 Complete

Before moving to Step 2, ensure:

- [ ] Assessment issue created with comprehensive prompt
- [ ] Issue includes current state, goals, and requirements
- [ ] Critical constraints highlighted (e.g., every minute scheduling)
- [ ] Requested comparison of multiple Azure options
- [ ] Copilot has responded with detailed analysis
- [ ] Response includes Azure deployment comparison
- [ ] Response provides recommendations

## 🎓 Key Takeaways

1. **Be specific** - Detailed prompts get better responses
2. **Provide context** - Copilot needs to understand your application
3. **Structure requests** - Use sections, bullets, and checklists
4. **Highlight constraints** - Critical requirements should be obvious
5. **Ask for options** - Comparisons help make informed decisions
6. **Be patient** - Quality analysis takes time

## 📚 Additional Resources

- [Effective Prompting Guide](https://docs.github.com/en/copilot/using-github-copilot/prompt-engineering-for-github-copilot)
- [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Java 17 Migration Guide](https://docs.oracle.com/en/java/javase/17/migrate/)

---

## 🎯 Next Step

Once Copilot has provided the assessment, you're ready to review it!

**→ Continue to [Step 2: Review Assessment](step-02-review-assessment.md)**
