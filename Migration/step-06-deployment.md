# Step 6: Azure Deployment

**Duration**: 45 minutes

## 🎯 Objectives

- Deploy the migrated application to Azure
- Configure Azure Container Apps
- Verify functionality in the cloud
- Set up monitoring and logging
- Understand Azure deployment patterns

## 📋 Prerequisites

- [ ] Completed Step 5: Local Testing
- [ ] Azure subscription (free tier works)
- [ ] Azure CLI installed
- [ ] Docker installed and working
- [ ] Application tested locally
- [ ] Docker image builds successfully

## 🏗️ Deployment Architecture

**Target**: Azure Container Apps

```
┌─────────────────────────────────────┐
│   Azure Container Registry (ACR)   │
│   (Stores Docker Image)             │
└────────────┬────────────────────────┘
             │ docker push
             ↓
┌─────────────────────────────────────┐
│  Azure Container Apps Environment   │
│  ┌───────────────────────────────┐  │
│  │  Message Service Container    │  │
│  │  ┌─────────────────────────┐  │  │
│  │  │  REST API (Port 8080)   │  │  │
│  │  │  Scheduled Task (1 min) │  │  │
│  │  │  H2 Database (Memory)   │  │  │
│  │  └─────────────────────────┘  │  │
│  └───────────────────────────────┘  │
│         ↑                            │
│         │ HTTPS                      │
└─────────┴─────────────────────── ────┘
          │
          ↓
     Internet Users
```

## 🔧 Installation & Setup

### Step 6.1: Install Azure CLI

**Check if already installed**:
```powershell
az --version
```

**If not installed**:
```powershell
# Download and install Azure CLI
# Visit: https://aka.ms/installazurecliwindows
# Or use winget:
winget install Microsoft.AzureCLI
```

### Step 6.2: Login to Azure

```powershell
# Login to Azure
az login

# Browser will open for authentication
# Select your account and subscription
```

**Verify login**:
```powershell
# List subscriptions
az account list --output table

# Set default subscription (if multiple)
az account set --subscription "<Subscription Name or ID>"
```

### Step 6.3: Install Container Apps Extension

```powershell
# Add the containerapp extension
az extension add --name containerapp --upgrade

# Register providers
az provider register --namespace Microsoft.App
az provider register --namespace Microsoft.OperationalInsights
```

## 📦 Preparing for Deployment

### Step 6.4: Create Resource Group

```powershell
# Set variables
$RESOURCE_GROUP="rg-message-service-workshop"
$LOCATION="eastus"

# Create resource group
az group create `
  --name $RESOURCE_GROUP `
  --location $LOCATION

# Expected output: JSON with provisioningState: "Succeeded"
```

### Step 6.5: Create Container Registry

```powershell
# Set ACR name (must be globally unique, lowercase, no hyphens)
$ACR_NAME="acrmessageservice$((Get-Random -Maximum 9999))"

# Create Azure Container Registry
az acr create `
  --resource-group $RESOURCE_GROUP `
  --name $ACR_NAME `
  --sku Basic `
  --admin-enabled true

# Get ACR login server
$ACR_SERVER=$(az acr show --name $ACR_NAME --query loginServer --output tsv)
Write-Host "ACR Server: $ACR_SERVER"

# Expected: <acrname>.azurecr.io
```

### Step 6.6: Build and Push Docker Image

**Option A: Build Locally and Push**

```powershell
# Build JAR file
mvn clean package -DskipTests

# Login to ACR
az acr login --name $ACR_NAME

# Build image with ACR tag
$IMAGE_NAME="$ACR_SERVER/message-service:v1"
docker build -t $IMAGE_NAME .

# Push to ACR
docker push $IMAGE_NAME

# Verify image in ACR
az acr repository list --name $ACR_NAME --output table
```

**Option B: Build in Azure (Recommended for workshops)**

```powershell
# ACR can build the image for you
az acr build `
  --registry $ACR_NAME `
  --image message-service:v1 `
  --file Dockerfile `
  .

# Faster, no local Docker push needed
```

### Step 6.7: Get ACR Credentials

```powershell
# Get username
$ACR_USERNAME=$(az acr credential show --name $ACR_NAME --query username --output tsv)

# Get password
$ACR_PASSWORD=$(az acr credential show --name $ACR_NAME --query "passwords[0].value" --output tsv)

# Store for next steps
Write-Host "ACR Username: $ACR_USERNAME"
Write-Host "ACR Password: [hidden]"
```

## ☁️ Deploy to Azure Container Apps

### Step 6.8: Create Container Apps Environment

```powershell
# Set environment name
$ENVIRONMENT="env-message-service"

# Create environment (this takes 3-5 minutes)
az containerapp env create `
  --name $ENVIRONMENT `
  --resource-group $RESOURCE_GROUP `
  --location $LOCATION

# Wait for completion
# Expected: provisioningState: "Succeeded"
```

### Step 6.9: Deploy Container App

```powershell
# Set app name
$APP_NAME="app-message-service"

# Deploy the application
az containerapp create `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --environment $ENVIRONMENT `
  --image "$ACR_SERVER/message-service:v1" `
  --registry-server $ACR_SERVER `
  --registry-username $ACR_USERNAME `
  --registry-password $ACR_PASSWORD `
  --target-port 8080 `
  --ingress external `
  --min-replicas 1 `
  --max-replicas 1 `
  --cpu 1.0 `
  --memory 2.0Gi

# Deployment takes 2-4 minutes
```

**Parameters Explained**:
- `--target-port 8080`: Application listens on 8080
- `--ingress external`: Accessible from internet
- `--min-replicas 1`: Always run 1 instance (for scheduled task)
- `--max-replicas 1`: Don't scale (H2 in-memory limitation)
- `--cpu 1.0`: 1 vCPU
- `--memory 2.0Gi`: 2 GB RAM (Java needs more memory)

### Step 6.10: Get Application URL

```powershell
# Get the FQDN
$APP_URL=$(az containerapp show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --query properties.configuration.ingress.fqdn `
  --output tsv)

Write-Host "Application URL: https://$APP_URL"

# Test endpoint
Invoke-WebRequest -Uri "https://$APP_URL/api/messages" -UseBasicParsing
```

## 🧪 Testing Azure Deployment

### Test 1: API Endpoints

```powershell
# Get all messages
$response = Invoke-RestMethod -Uri "https://$APP_URL/api/messages" -Method Get
$response | ConvertTo-Json

# Create message
$body = @{
    content = "Deployed to Azure Container Apps!"
    author = "azure-user"
} | ConvertTo-Json

$response = Invoke-RestMethod `
  -Uri "https://$APP_URL/api/messages" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"

$response | ConvertTo-Json
```

### Test 2: Verify Scheduled Task

```powershell
# View logs to see scheduled task
az containerapp logs show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --follow

# Look for output every 60 seconds:
# "Message Statistics Task - Executing"
# Press Ctrl+C to stop following
```

**⚠️ Verify**: Task should execute every 60 seconds consistently

### Test 3: H2 Console Access

**Note**: H2 console may not be accessible externally for security reasons.

To enable (development/workshop only):
```powershell
# Update environment variables
az containerapp update `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --set-env-vars "SPRING_H2_CONSOLE_SETTINGS_WEB-ALLOW-OTHERS=true"

# Access H2 console
# https://$APP_URL/h2-console
```

**Production**: Use Azure SQL Database or external PostgreSQL instead of H2.

## 📊 Monitoring and Logging

### View Application Logs

```powershell
# Stream logs in real-time
az containerapp logs show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --follow

# Get recent logs (last 100 lines)
az containerapp logs show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --tail 100
```

### View Container App Metrics

```powershell
# Get app details including replica status
az containerapp show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --output table

# Check revision status
az containerapp revision list `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --output table
```

### Set Up Log Analytics (Optional)

Container Apps automatically sends logs to Log Analytics.

**Access in Azure Portal**:
1. Go to Azure Portal
2. Navigate to your Container App
3. Click **Logs** in left menu
4. Run queries:

```kusto
// View all logs from last hour
ContainerAppConsoleLogs_CL
| where TimeGenerated > ago(1h)
| project TimeGenerated, Log_s
| order by TimeGenerated desc

// Find scheduled task executions
ContainerAppConsoleLogs_CL
| where Log_s contains "Message Statistics Task"
| project TimeGenerated, Log_s
| order by TimeGenerated desc
```

## 🔄 Updating the Application

### Update Application Code

After making changes:

```powershell
# Rebuild JAR
mvn clean package -DskipTests

# Build and push new image version
az acr build `
  --registry $ACR_NAME `
  --image message-service:v2 `
  --file Dockerfile `
  .

# Update container app
az containerapp update `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --image "$ACR_SERVER/message-service:v2"

# Container will restart with new version
```

### Check Deployment Status

```powershell
# Watch revision provisioning
az containerapp revision list `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --output table

# Old revision will be deactivated
# New revision should show "Running"
```

## 💰 Cost Management

### View Current Costs

```powershell
# Estimated monthly cost for this setup:
# - Container Apps: $0-50/month (mostly idle)
# - Container Registry: $5/month (Basic tier)
# - Total: ~$5-55/month
```

**Cost Optimization**:
- Use Azure Free Trial credits
- Stop container when not in use:
  ```powershell
  az containerapp update --name $APP_NAME --resource-group $RESOURCE_GROUP --min-replicas 0
  ```
- Delete resources after workshop

### Delete Resources

```powershell
# Delete entire resource group (removes everything)
az group delete --name $RESOURCE_GROUP --yes --no-wait

# This will delete:
# - Container App
# - Container Apps Environment
# - Container Registry
# - All associated resources
```

## 🔐 Security Best Practices

### Use Managed Identity (Production)

Instead of admin credentials:

```powershell
# Enable managed identity
az containerapp identity assign `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --system-assigned

# Grant ACR pull permissions to managed identity
$PRINCIPAL_ID=$(az containerapp show --name $APP_NAME --resource-group $RESOURCE_GROUP --query identity.principalId --output tsv)

az role assignment create `
  --assignee $PRINCIPAL_ID `
  --scope $(az acr show --name $ACR_NAME --query id --output tsv) `
  --role AcrPull
```

### Enable HTTPS Only

```powershell
# Container Apps uses HTTPS by default
# Verify:
az containerapp ingress show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP
```

### Add Custom Domain (Optional)

```powershell
# Add custom domain
az containerapp hostname add `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --hostname "api.yourdomain.com"

# Requires DNS configuration
```

## 🎯 Production Considerations

### Replace H2 with Persistent Database

For production, use Azure Database for PostgreSQL:

```powershell
# Create PostgreSQL server
az postgres flexible-server create `
  --resource-group $RESOURCE_GROUP `
  --name "psql-message-service" `
  --admin-user myadmin `
  --admin-password "MyPassword123!" `
  --sku-name Standard_B1ms

# Update application.properties:
# spring.datasource.url=jdbc:postgresql://psql-message-service.postgres.database.azure.com:5432/messagedb
# spring.datasource.username=myadmin
# spring.datasource.password=MyPassword123!
# spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Enable Scaling (With External DB)

```powershell
# Allow scaling when using external database
az containerapp update `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --min-replicas 1 `
  --max-replicas 10 `
  --scale-rule-name "http-scale" `
  --scale-rule-type "http" `
  --scale-rule-http-concurrency 50
```

**Note**: Scheduled tasks need special handling with multiple replicas (use Azure Functions or leader election)

### Set Up CI/CD with GitHub Actions

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Azure Container Apps

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build with Maven
        run: mvn clean package -DskipTests
      
      - name: Login to Azure
        uses: azure/login@v1
        with:
          creds: ${{ secrets.AZURE_CREDENTIALS }}
      
      - name: Build and push image
        run: |
          az acr build \
            --registry ${{ secrets.ACR_NAME }} \
            --image message-service:${{ github.sha }} \
            --file Dockerfile \
            .
      
      - name: Deploy to Container Apps
        run: |
          az containerapp update \
            --name app-message-service \
            --resource-group rg-message-service-workshop \
            --image ${{ secrets.ACR_NAME }}.azurecr.io/message-service:${{ github.sha }}
```

## ✅ Deployment Checklist

Verify successful deployment:

### Infrastructure
- [ ] Resource group created
- [ ] Container Registry created and accessible
- [ ] Docker image pushed to ACR
- [ ] Container Apps Environment created
- [ ] Container App deployed and running

### Application
- [ ] Application starts without errors
- [ ] ⚠️ Scheduled task runs every 60 seconds
- [ ] All API endpoints accessible via HTTPS
- [ ] Database (H2) initializes correctly
- [ ] Sample data loads successfully

### Monitoring
- [ ] Logs accessible via Azure CLI
- [ ] Log Analytics showing data
- [ ] No error logs or exceptions
- [ ] Application metrics available

### Testing
- [ ] Can create messages via API
- [ ] Can read messages via API
- [ ] Can update messages via API
- [ ] Can delete messages via API
- [ ] Search functionality works
- [ ] Scheduled task visible in logs

## 🎓 Alternative Deployment Options

### Option 1: Azure App Service (Traditional)

```powershell
# Create App Service Plan
az appservice plan create `
  --name "plan-message-service" `
  --resource-group $RESOURCE_GROUP `
  --sku B1 `
  --is-linux

# Create Web App
az webapp create `
  --name "app-message-service-webapp" `
  --resource-group $RESOURCE_GROUP `
  --plan "plan-message-service" `
  --runtime "JAVA:17-java17"

# Deploy JAR
az webapp deploy `
  --resource-group $RESOURCE_GROUP `
  --name "app-message-service-webapp" `
  --src-path target/message-service-1.0.0.jar `
  --type jar
```

**Pros**: Traditional, familiar, easy deployment  
**Cons**: More expensive, less cloud-native

### Option 2: Azure Functions (Scheduled Task Only)

For the scheduled task separately:

```powershell
# Create Function App
az functionapp create `
  --name "func-message-stats" `
  --resource-group $RESOURCE_GROUP `
  --consumption-plan-location $LOCATION `
  --runtime java `
  --runtime-version 17 `
  --functions-version 4
```

**Pros**: Perfect for scheduled tasks, cost-effective  
**Cons**: Need to migrate API separately, serverless constraints

## 🛠️ Troubleshooting

### Issue: Container App Won't Start

```powershell
# Check logs for errors
az containerapp logs show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP

# Common issues:
# - Image pull errors (check ACR credentials)
# - Application startup failures (check JAR is in image)
# - Port misconfiguration (should be 8080)
```

### Issue: Can't Access Application URL

```powershell
# Verify ingress is external
az containerapp ingress show `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP

# Should show: "external": true
```

### Issue: Scheduled Task Not Running

Check logs for:
- @EnableScheduling in Application class
- No exceptions during task execution
- Task not being killed mid-execution

### Issue: High Memory Usage

```powershell
# Increase memory allocation
az containerapp update `
  --name $APP_NAME `
  --resource-group $RESOURCE_GROUP `
  --memory 3.0Gi
```

## ✅ Checklist - Step 6 Complete

Workshop completion checklist:

- [ ] Azure CLI installed and configured
- [ ] Logged into Azure subscription
- [ ] Resource group created
- [ ] Container Registry created
- [ ] Docker image built and pushed to ACR
- [ ] Container Apps Environment created
- [ ] Application deployed to Container Apps
- [ ] Application accessible via HTTPS
- [ ] All API endpoints tested and working
- [ ] Scheduled task verified running every 60 seconds
- [ ] Logs accessible and readable
- [ ] Understand deployment process
- [ ] Know how to update and redeploy
- [ ] Aware of costs and cleanup process

## 🎓 Key Takeaways

1. **Cloud-Native Deployment** - Containers are the modern standard
2. **Azure Container Apps** - Great balance of simplicity and power
3. **Infrastructure as Code** - Azure CLI scripts are reproducible
4. **Monitoring Matters** - Always verify apps work in cloud
5. **Cost Awareness** - Clean up resources when done
6. **Security First** - Use managed identities, HTTPS, private registries
7. **Iterate Quickly** - Azure makes updates fast and easy

## 🏆 Congratulations!

You've successfully:
✅ Migrated a legacy Java 1.8 / Spring 4.x application  
✅ Modernized to JDK 17 / Spring Boot 3.x  
✅ Containerized with Docker  
✅ Deployed to Azure Container Apps  
✅ Learned to use GitHub Copilot as an autonomous team member  

## 📚 Next Steps

### Continue Learning:
1. **Add Azure SQL Database** - Replace H2 with persistent storage
2. **Implement CI/CD** - Automate deployment with GitHub Actions
3. **Add API Authentication** - Secure with Azure AD or API keys
4. **Enable Monitoring** - Set up Application Insights
5. **Try Azure Spring Apps** - Purpose-built for Spring Boot
6. **Explore AKS** - Kubernetes for complex microservices

### Apply to Your Projects:
- Use this migration pattern for your legacy applications
- Leverage GitHub Copilot for code modernization
- Adopt Azure Container Apps for new cloud deployments
- Implement CI/CD pipelines for automation

## 🤝 Sharing Your Success

Share your experience:
- Blog about your migration journey
- Present to your team
- Contribute improvements to this workshop
- Help others migrate their legacy applications

---

## 🎉 Workshop Complete!

Thank you for completing the Java Application Migration Workshop!

**Questions or Issues?** 
- Review workshop documentation
- Check Azure documentation
- Ask GitHub Copilot for help!

**Happy Coding! 🚀**
