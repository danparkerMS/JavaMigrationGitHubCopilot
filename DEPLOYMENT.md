# Azure Deployment Guide: Message Service

This guide covers deploying the Message Service Spring Boot 3.x application to Azure Container Apps.

## Prerequisites

- Azure CLI installed and configured (`az login`)
- Docker installed (for local building/testing)
- Azure subscription with Container Apps support

## Quick Start

### 1. Set Environment Variables

```bash
# Configure your settings
export RESOURCE_GROUP="message-service-rg"
export LOCATION="eastus"
export ENVIRONMENT="message-service-env"
export APP_NAME="message-service"
export REGISTRY_NAME="messageservicereg"  # Must be globally unique
```

### 2. Create Resource Group

```bash
az group create --name $RESOURCE_GROUP --location $LOCATION
```

### 3. Create Azure Container Registry

```bash
# Create the registry
az acr create \
  --resource-group $RESOURCE_GROUP \
  --name $REGISTRY_NAME \
  --sku Basic \
  --admin-enabled true

# Get the login server
export ACR_LOGIN_SERVER=$(az acr show \
  --name $REGISTRY_NAME \
  --query loginServer \
  --output tsv)
```

### 4. Build and Push Docker Image

```bash
# Build locally (or use ACR build)
docker build -t $ACR_LOGIN_SERVER/message-service:latest .

# Login to ACR
az acr login --name $REGISTRY_NAME

# Push to ACR
docker push $ACR_LOGIN_SERVER/message-service:latest

# Alternatively, build directly in ACR:
az acr build \
  --registry $REGISTRY_NAME \
  --image message-service:latest \
  .
```

### 5. Create Container Apps Environment

```bash
az containerapp env create \
  --name $ENVIRONMENT \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION
```

### 6. Deploy Container App

```bash
# Get ACR credentials
export ACR_PASSWORD=$(az acr credential show \
  --name $REGISTRY_NAME \
  --query "passwords[0].value" \
  --output tsv)

# Deploy the app
az containerapp create \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --environment $ENVIRONMENT \
  --image $ACR_LOGIN_SERVER/message-service:latest \
  --registry-server $ACR_LOGIN_SERVER \
  --registry-username $REGISTRY_NAME \
  --registry-password $ACR_PASSWORD \
  --target-port 8080 \
  --ingress external \
  --min-replicas 1 \
  --max-replicas 3 \
  --cpu 0.5 \
  --memory 1.0Gi \
  --env-vars "SPRING_PROFILES_ACTIVE=production"
```

### 7. Get Application URL

```bash
az containerapp show \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --query properties.configuration.ingress.fqdn \
  --output tsv
```

## Configuration Details

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `default` |
| `SERVER_PORT` | Application port | `8080` |
| `JAVA_OPTS` | JVM options | (empty) |

### Health Checks

The application exposes health endpoints via Spring Boot Actuator:

- **Liveness**: `GET /actuator/health`
- **Readiness**: `GET /actuator/health`

Container Apps will automatically use these for health monitoring.

### Scaling

The deployment is configured with:
- Minimum replicas: 1
- Maximum replicas: 3
- CPU: 0.5 cores
- Memory: 1.0 GB

Adjust these based on your workload requirements.

## Scheduled Task Behavior

The application includes a scheduled task that runs every 60 seconds to report message statistics.

**Important**: In Azure Container Apps:
- The scheduled task runs within each container replica
- If you have multiple replicas, the task will run in each one
- For single-instance scheduled tasks, set `--min-replicas 1 --max-replicas 1`

## Monitoring and Troubleshooting

### View Logs

```bash
# Stream logs
az containerapp logs show \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --follow

# View recent logs
az containerapp logs show \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --type console
```

### Check Application Status

```bash
az containerapp show \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --query "{Status:properties.runningStatus, Replicas:properties.template.containers[0].resources}"
```

### View Revisions

```bash
az containerapp revision list \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --output table
```

## Updating the Application

### Deploy New Version

```bash
# Build new image
docker build -t $ACR_LOGIN_SERVER/message-service:v2 .
docker push $ACR_LOGIN_SERVER/message-service:v2

# Update container app
az containerapp update \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --image $ACR_LOGIN_SERVER/message-service:v2
```

## Testing the Deployed Application

```bash
# Get the application URL
APP_URL=$(az containerapp show \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --query properties.configuration.ingress.fqdn \
  --output tsv)

# Test endpoints
curl https://$APP_URL/api/messages
curl https://$APP_URL/actuator/health

# Create a message
curl -X POST https://$APP_URL/api/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello from Azure!","author":"azure-user"}'
```

## Cleanup

To remove all resources:

```bash
az group delete --name $RESOURCE_GROUP --yes --no-wait
```

## Alternative: Azure App Service

If you prefer Azure App Service over Container Apps:

```bash
# Create App Service Plan
az appservice plan create \
  --name message-service-plan \
  --resource-group $RESOURCE_GROUP \
  --sku B1 \
  --is-linux

# Create Web App
az webapp create \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --plan message-service-plan \
  --runtime "JAVA:17-java17"

# Deploy JAR
az webapp deploy \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --src-path target/message-service.jar \
  --type jar
```

## Cost Optimization

- Use the Consumption tier for Container Apps (pay per use)
- Set appropriate min/max replicas based on expected traffic
- Use Azure Container Registry Basic tier for development
- Consider using spot instances for non-production workloads
