# Step 5: Local Testing

**Duration**: 30 minutes

## 🎯 Objectives

- Build the migrated application
- Run and test locally
- Verify all functionality works
- Test with Docker
- Validate migration success

## 📋 Prerequisites

- [ ] Completed Step 4: Review Migration Work
- [ ] PR merged to main branch
- [ ] JDK 17+ installed and configured
- [ ] Maven 3.6+ installed
- [ ] Docker installed (for container testing)
- [ ] Git repository up to date

## 🚀 Getting Started

### Step 5.1: Pull Latest Code

```powershell
# Navigate to your project directory
cd C:\Workspace\ProfessionServicesAccount\NYTOUR\JavaSample

# Pull the merged changes
git checkout main
git pull origin main

# Verify you have the migrated code
ls pom.xml  # Should show Spring Boot 3.x
```

### Step 5.2: Verify Java Version

```powershell
# Check Java version
java -version

# Should show: openjdk version "17.x.x" or higher
# If not, configure JAVA_HOME to point to JDK 17+
```

**If JDK 17 not found**:
```powershell
# Windows - Set JAVA_HOME temporarily
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verify again
java -version
```

## 🔨 Building the Application

### Step 5.3: Clean Build

```powershell
# Clean previous builds
mvn clean

# Build the application
mvn package

# Expected output:
# [INFO] BUILD SUCCESS
# [INFO] Total time: ...
# [INFO] Finished at: ...
```

**What Happens**:
- Compiles Java 17 code
- Runs tests (if any)
- Creates executable JAR in `target/` directory
- JAR name: `message-service-<version>.jar` or similar

### Troubleshooting Build Issues

#### Issue: "invalid target release: 17"

**Problem**: Maven using wrong JDK

**Solution**:
```powershell
# Check Maven's Java version
mvn -version

# Should show Java version 17.x
# If not, ensure JAVA_HOME points to JDK 17
```

#### Issue: Package javax.* does not exist

**Problem**: Code not fully migrated

**Solution**:
```powershell
# Search for remaining javax imports
Get-ChildItem -Recurse -Include *.java | Select-String "import javax\.(persistence|validation|servlet)" 

# Report findings to Copilot for fix
```

#### Issue: Build fails with dependency errors

**Problem**: Corrupted Maven cache

**Solution**:
```powershell
# Clean Maven cache
mvn dependency:purge-local-repository
mvn clean install
```

## 🏃 Running the Application

### Step 5.4: Run with Maven

```powershell
# Run using Spring Boot Maven plugin
mvn spring-boot:run

# Application should start on port 8080
# Look for: "Started Application in X seconds"
```

**Expected Console Output**:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.x.x)

2025-11-16T10:30:00.123  INFO --- [main] c.n.demo.Application
: Starting Application using Java 17.0.x
...
2025-11-16T10:30:05.456  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer
: Tomcat started on port 8080 (http)
2025-11-16T10:30:05.500  INFO --- [main] c.n.demo.Application
: Started Application in 5.377 seconds
```

### Step 5.5: Verify Scheduled Task

Within 60 seconds, you should see:

```
2025-11-16T10:31:00.000  INFO --- [scheduling-1] c.n.d.task.MessageScheduledTask
: ========================================
2025-11-16T10:31:00.000  INFO --- [scheduling-1] c.n.d.task.MessageScheduledTask
: Message Statistics Task - Executing
2025-11-16T10:31:00.000  INFO --- [scheduling-1] c.n.d.task.MessageScheduledTask
: Execution Time: 2025-11-16 10:31:00
2025-11-16T10:31:00.010  INFO --- [scheduling-1] c.n.d.task.MessageScheduledTask
: Total Messages: 5
2025-11-16T10:31:00.010  INFO --- [scheduling-1] c.n.d.task.MessageScheduledTask
: Active Messages: 5
2025-11-16T10:31:00.010  INFO --- [scheduling-1] c.n.d.task.MessageScheduledTask
: ========================================
```

**⚠️ CRITICAL CHECK**: Verify task runs every 60 seconds (not 30, not 120)

Set a timer and watch for 3 executions to confirm timing.

## 🧪 Testing the API

### Step 5.6: Open New Terminal

Keep the application running in one terminal, open a new PowerShell window for testing.

### Test 1: Health Check

```powershell
# Simple connectivity test
Invoke-WebRequest -Uri http://localhost:8080 -UseBasicParsing

# Should return 200 OK or the home page
```

### Test 2: Get All Messages

```powershell
# Get all messages
$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages -Method Get
$response | ConvertTo-Json -Depth 3

# Expected: JSON array with 5 sample messages
```

**Example Output**:
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "content": "Welcome to the Message Service!",
      "author": "admin",
      "createdDate": "2025-11-16T10:30:00",
      "active": true
    },
    ...
  ],
  "count": 5
}
```

### Test 3: Get Message by ID

```powershell
# Get specific message
$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages/1 -Method Get
$response | ConvertTo-Json

# Expected: Single message object
```

### Test 4: Create New Message

```powershell
# Create message
$body = @{
    content = "Testing the migrated application!"
    author = "tester"
} | ConvertTo-Json

$headers = @{
    "Content-Type" = "application/json"
}

$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages `
    -Method Post `
    -Body $body `
    -Headers $headers

$response | ConvertTo-Json

# Expected: Success response with new message (id: 6)
```

### Test 5: Update Message

```powershell
# Update the message we just created
$body = @{
    content = "Updated message content"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages/6 `
    -Method Put `
    -Body $body `
    -Headers $headers

$response | ConvertTo-Json

# Expected: Success response with updated message
```

### Test 6: Search Messages

```powershell
# Search for keyword
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/messages/search?keyword=migrated" -Method Get
$response | ConvertTo-Json

# Expected: Messages containing "migrated"
```

### Test 7: Delete Message

```powershell
# Delete message
$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages/6 -Method Delete
$response | ConvertTo-Json

# Expected: Success response

# Verify deletion
$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages -Method Get
$response.count

# Expected: 5 (back to original count)
```

## 🗄️ Testing H2 Console

### Step 5.7: Access H2 Console

1. Open browser to: http://localhost:8080/h2-console

2. **Login Credentials** (from application.properties):
   - **JDBC URL**: `jdbc:h2:mem:messagedb`
   - **User Name**: `sa`
   - **Password**: (leave empty)

3. Click **Connect**

4. **Run SQL Query**:
   ```sql
   SELECT * FROM MESSAGES;
   ```

5. **Verify Data**:
   - Should see all messages
   - Check `CREATED_DATE` column format
   - Verify `IS_ACTIVE` column values

## 🐳 Docker Testing

### Step 5.8: Build Docker Image

```powershell
# Ensure JAR is built
mvn clean package -DskipTests

# Build Docker image
docker build -t message-service:1.0 .

# Expected output:
# Successfully built <image-id>
# Successfully tagged message-service:1.0

# Verify image
docker images | Select-String "message-service"
```

### Step 5.9: Run Docker Container

```powershell
# Stop the Maven-run app first (Ctrl+C in that terminal)

# Run container
docker run -d `
  --name message-service `
  -p 8080:8080 `
  message-service:1.0

# Check container status
docker ps

# View logs
docker logs -f message-service
```

**Look for**:
- Application starts successfully
- Scheduled task executes every minute
- No errors in logs

### Step 5.10: Test API in Container

Run the same API tests from Step 5.6:

```powershell
# Test endpoint
Invoke-RestMethod -Uri http://localhost:8080/api/messages -Method Get | ConvertTo-Json
```

**Verify**:
- [ ] API responds correctly
- [ ] Scheduled task runs every 60 seconds
- [ ] Data persists during container lifetime
- [ ] No errors in logs

### Step 5.11: Clean Up Docker

```powershell
# Stop container
docker stop message-service

# Remove container
docker rm message-service

# (Optional) Remove image
docker rmi message-service:1.0
```

## ✅ Migration Validation Checklist

Complete testing validation:

### Build & Run
- [ ] Maven build succeeds with no errors
- [ ] Application starts with Spring Boot
- [ ] No startup errors or warnings
- [ ] Runs on port 8080

### Scheduled Task
- [ ] ⚠️ **CRITICAL**: Task runs exactly every 60 seconds
- [ ] Task logs message statistics
- [ ] Task continues running reliably
- [ ] No exceptions in task execution

### REST API
- [ ] GET /api/messages returns all messages
- [ ] GET /api/messages/{id} returns specific message
- [ ] POST /api/messages creates new message
- [ ] PUT /api/messages/{id} updates message
- [ ] DELETE /api/messages/{id} deletes message
- [ ] GET /api/messages/search works correctly
- [ ] All responses use correct format

### Database
- [ ] H2 database initializes correctly
- [ ] Sample data loads successfully
- [ ] CRUD operations work
- [ ] H2 console accessible
- [ ] Date fields stored correctly (LocalDateTime)

### Docker
- [ ] Docker image builds successfully
- [ ] Container runs without errors
- [ ] API accessible from host
- [ ] Scheduled task works in container
- [ ] Logs viewable with docker logs

### Code Quality
- [ ] No javax.* packages used
- [ ] All dates use java.time
- [ ] SLF4J logging throughout
- [ ] No deprecated APIs
- [ ] No compilation warnings

## 🔍 Troubleshooting

### Issue: Port 8080 Already in Use

```powershell
# Find process using port 8080
Get-NetTCPConnection -LocalPort 8080 | Select-Object OwningProcess

# Kill the process (replace PID)
Stop-Process -Id <PID> -Force

# Or change port in application.properties:
# server.port=8081
```

### Issue: Scheduled Task Not Running

**Check**:
1. `@EnableScheduling` in Application class
2. `@Component` on MessageScheduledTask
3. `fixedDelay = 60000` (not 6000 or 600000)
4. No exceptions in logs

**Debug**:
```java
// Temporarily add to MessageScheduledTask
@PostConstruct
public void init() {
    logger.info("MessageScheduledTask initialized - scheduling enabled");
}
```

### Issue: H2 Console Won't Connect

**Check application.properties**:
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Verify URL**: http://localhost:8080/h2-console (exact path)

### Issue: API Returns 404

**Check**:
1. Controller path: `@RequestMapping("/api/messages")`
2. Application actually started (check logs)
3. Using correct port
4. Spring Boot Web starter in pom.xml

### Issue: Docker Container Exits Immediately

```powershell
# Check container logs
docker logs message-service

# Run interactively to see errors
docker run -it --rm -p 8080:8080 message-service:1.0
```

Common causes:
- JAR file not in image
- Wrong Java version in Dockerfile
- Port conflict
- Application startup error

## 📊 Performance Testing (Optional)

### Load Test with Multiple Requests

```powershell
# Create multiple messages
for ($i=1; $i -le 10; $i++) {
    $body = @{
        content = "Load test message $i"
        author = "loadtest"
    } | ConvertTo-Json
    
    Invoke-RestMethod -Uri http://localhost:8080/api/messages `
        -Method Post `
        -Body $body `
        -Headers @{"Content-Type"="application/json"} `
        | Out-Null
    
    Write-Host "Created message $i"
}

# Verify all created
$response = Invoke-RestMethod -Uri http://localhost:8080/api/messages -Method Get
Write-Host "Total messages: $($response.count)"
```

## ✅ Checklist - Step 5 Complete

Before moving to Step 6:

- [ ] Application builds successfully with Maven
- [ ] Runs locally with `mvn spring-boot:run`
- [ ] Scheduled task verified running every 60 seconds
- [ ] All REST API endpoints tested and working
- [ ] H2 console accessible and functional
- [ ] Docker image builds successfully
- [ ] Container runs and API accessible
- [ ] No errors or warnings in logs
- [ ] All migration changes validated
- [ ] Ready for Azure deployment

## 🎓 Key Takeaways

1. **Local First** - Always test locally before cloud deployment
2. **Verify Timing** - Don't assume scheduled tasks work correctly
3. **Test Thoroughly** - Check all endpoints, not just GET
4. **Docker Matters** - Container behavior can differ from local
5. **Logs Are Key** - Watch logs for issues even when app "works"
6. **Document Issues** - Note any problems for deployment phase

## 📚 Additional Resources

- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html) (for monitoring)

---

## 🎯 Next Step

With local testing complete and successful, you're ready to deploy to Azure!

**→ Continue to [Step 6: Azure Deployment](step-06-deployment.md)**
