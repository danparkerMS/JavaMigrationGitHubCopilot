# Step 0: Introduction to GitHub Copilot

**Duration**: 15 minutes

## 🎯 Objectives

- Understand GitHub Copilot's two primary modes
- Learn when to use each mode effectively
- Prepare for the migration workshop

## 📖 What is GitHub Copilot?

GitHub Copilot is an AI-powered coding assistant that can work in two distinct modes:

### Mode 1: Pair Programmer 🤝

**How it works**: Inline suggestions as you type in your IDE (VS Code, IntelliJ, etc.)

**Best for**:
- Writing individual functions or methods
- Completing code snippets
- Getting quick suggestions
- Real-time coding assistance
- Implementing well-defined, small tasks

**Example**: Type a function signature, and Copilot suggests the implementation.

### Mode 2: Autonomous Team Member 🤖

**How it works**: Assign tasks via GitHub Issues using `@copilot`

**Best for**:
- Analyzing entire codebases
- Planning complex migrations or refactoring
- Making changes across multiple files
- Generating documentation
- Providing architectural recommendations
- **Application modernization** (like our workshop!)

**Example**: Create an issue asking Copilot to analyze your application and recommend migration strategies.

## 🔑 Key Difference

| Aspect | Pair Programmer | Autonomous Team Member |
|--------|-----------------|------------------------|
| **Scope** | Single file/function | Entire codebase |
| **Interaction** | Inline, real-time | Issue-based, async |
| **Context** | Current file | Full repository |
| **Output** | Code suggestions | Analysis, plans, PRs |
| **Use Case** | Writing code | Planning & execution |

## 🎓 Why Use Copilot for Migration?

Application migration involves:

1. **Analysis** - Understanding legacy code and dependencies
2. **Planning** - Choosing the right migration strategy
3. **Implementation** - Updating code across many files
4. **Testing** - Ensuring functionality is preserved
5. **Documentation** - Recording changes made

**Copilot as an autonomous team member excels at all of these!**

## 🏗️ Workshop Context

In this workshop, you'll use Copilot's **autonomous team member** mode to:

### Assessment Phase (Steps 1-2)
- Analyze the legacy Java application
- Identify migration challenges
- Compare Azure deployment options
- Recommend modernization strategies

### Implementation Phase (Steps 3-4)
- Implement the migration across all files
- Update dependencies and configurations
- Modernize deprecated APIs
- Create deployment documentation

### Testing & Deployment (Steps 5-6)
- Build and test the migrated application
- Deploy to Azure cloud services

## 💡 Prompting Best Practices

When working with Copilot, effective prompts should:

### ✅ DO:
- Be specific about requirements
- Provide context about the application
- Reference previous issues/conversations
- Ask for comparisons and trade-offs
- Request documentation alongside code
- Break down complex tasks

### ❌ DON'T:
- Be vague or overly broad
- Assume Copilot knows your constraints
- Skip important details
- Ask for everything at once
- Forget to specify critical requirements (e.g., "runs every minute")

## 🎯 Example: Good vs Bad Prompts

### ❌ Bad Prompt
```
Migrate this Java app
```

### ✅ Good Prompt
```
@copilot Please analyze this Java application and provide a migration assessment:

Current State:
- JDK 1.8
- Spring Boot 2.7.18 (last 2.x version before 3.x)
- WAR deployment
- H2 in-memory database
- Scheduled task that must run every minute

Requirements:
1. Migrate to JDK 17 and Spring Boot 3.x
2. Deploy to Azure cloud services
3. Preserve all functionality including the scheduled task
4. Recommend 2-3 Azure deployment options with pros/cons
5. Provide cost and complexity comparison

Please include:
- Migration challenges and breaking changes
- Recommended approach
- Azure service options
- Estimated effort
```

**Why is this better?**
- ✅ Specifies current state
- ✅ Lists clear requirements
- ✅ Mentions critical constraint (every minute)
- ✅ Requests options and comparison
- ✅ Asks for specific deliverables

## 🚀 Getting Started

### Prerequisites Checklist

Before proceeding, ensure you have:

- [ ] GitHub account with Copilot access enabled
- [ ] Legacy application running successfully (see main README)
- [ ] Familiarity with Git and GitHub Issues
- [ ] Understanding of Java basics
- [ ] (Optional) Azure account for deployment

### Verify Copilot Access

1. Go to your GitHub account settings
2. Navigate to "Copilot" section
3. Ensure you have an active subscription
4. Verify "Copilot in GitHub" is enabled (not just IDE)

### Test Copilot in Issues

1. Create a test issue in any repository
2. Type `@copilot` in the issue description or comment
3. You should see Copilot mentioned/tagged
4. If not, contact your GitHub administrator

## 🎓 Workshop Flow

Here's how the workshop progresses:

```
Step 0 (Now)
  ↓
Step 1: Create Assessment Issue
  → Copilot analyzes the application
  ↓
Step 2: Review Assessment
  → You evaluate Copilot's recommendations
  ↓
Step 3: Create Migration Issue
  → Copilot creates a PR with migrated code
  ↓
Step 4: Review Migration
  → You examine the changes
  ↓
Step 5: Local Testing
  → Build and test the migrated app
  ↓
Step 6: Deployment
  → Deploy to Azure
```

## ✅ Checklist - Step 0 Complete

Before moving to Step 1, confirm:

- [ ] I understand the two modes of GitHub Copilot
- [ ] I know when to use the autonomous team member mode
- [ ] I understand prompting best practices
- [ ] I have verified my Copilot access
- [ ] The legacy application is running successfully
- [ ] I'm ready to create assessment issues

## 📚 Additional Resources

- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)
- [Copilot Chat in GitHub](https://docs.github.com/en/copilot/using-github-copilot/asking-github-copilot-questions-in-github)
- [Best Practices for Prompting Copilot](https://docs.github.com/en/copilot/using-github-copilot/prompt-engineering-for-github-copilot)

---

## 🎯 Next Step

You're now ready to begin the migration!

**→ Continue to [Step 1: Create Assessment Issue](step-01-create-assessment-issue.md)**
