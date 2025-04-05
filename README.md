# AWS TodoApp - SpringBoot with DynamoDB

## Project Overview
A containerized Todo web application built with Java SpringBoot and Thymeleaf that performs CRUD operations on AWS DynamoDB. The application is deployed on Amazon ECS with blue/green deployment capabilities and features a complete CI/CD pipeline using GitHub Actions.

## Architecture

### Components
- **Frontend**: Thymeleaf templates  
- **Backend**: Java SpringBoot REST API  
- **Database**: AWS DynamoDB  
- **Hosting**: Amazon ECS Tasks/Service with Application Load Balancer  
- **Containerization**: Docker  
- **CI/CD**: GitHub Actions and/or AWS CodePipeline  
- **Infrastructure as Code**: AWS SAM & CloudFormation  

### Deployment Architecture
- Custom VPC created via CloudFormation GitSync  
- DynamoDB table provisioned separately using AWS SAM  
- Application container deployed to Amazon ECS  
- Blue/Green deployment strategy for zero-downtime updates  

## Features
- Create, read, update, and delete Todo items  
- Responsive UI built with Thymeleaf templates  
- Containerized application for consistent deployment  
- Automated CI/CD pipeline  
- Infrastructure as Code for all AWS resources  

## Technical Implementation

### Prerequisites
- AWS Account with appropriate permissions  
- GitHub account  
- Docker installed locally for development  
- Java Development Kit (JDK) 11+  
- Maven or Gradle  

### Local Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/mawule-gabriel/GoTasksRe
   cd aws-GotasksRe
   ```

2. Configure AWS credentials:
   ```bash
   aws configure
   ```

3. Build the application:
   ```bash
   ./mvnw clean package
   ```

4. Run locally with DynamoDB Local:
   ```bash
   docker-compose up
   ```

5. Access the application at: `http://localhost:8080`

## AWS Infrastructure

### VPC and Networking
- Custom VPC with public and private subnets  
- NAT Gateway for private subnet internet access  
- Security Groups for ECS and ALB  
- Optional bastion host for accessing private resources  

### DynamoDB
Schema provisioned using AWS SAM template:
- Primary Key: `id` (String)

#### Entity Fields:
- `id` - String, partition key  
- `content` - String, contains the task description  
- `completed` - Boolean, tracks task completion status  

### ECS Configuration
- Fargate launch type  
- Task Definition with appropriate CPU and memory allocations  
- Service backed by Application Load Balancer  
- Blue/Green deployment configuration  

## CI/CD Pipeline

### GitHub Actions Workflow
1. **Build Stage**:
   - Checkout code  
   - Build Java application  
   - Build Docker image  
   - Push to Amazon ECR  

2. **Deploy Stage**:
   - Update ECS service  
   - Execute Blue/Green deployment  
   - Monitor deployment status  

### Blue/Green Deployment Process
1. A new ECS task definition revision is created  
2. New "green" tasks are deployed  
3. Traffic is gradually shifted from "blue" to "green" environment  
4. Old "blue" tasks are terminated once deployment is successful  

## Security Considerations
- IAM roles with least privilege principles  
- Security groups with minimal required access  
- No sensitive information in code repositories  
- VPC endpoint for DynamoDB access if using private subnets  

## Monitoring and Logging
- CloudWatch Logs for container logs  
- CloudWatch Metrics for ECS and DynamoDB monitoring  
- X-Ray for distributed tracing (optional)  

## Scaling
- Auto Scaling for ECS tasks based on CPU/memory utilization  
- DynamoDB provisioned with appropriate read/write capacity units  

## Troubleshooting
- Check CloudWatch Logs for application errors  
- Verify network connectivity between components  
- Ensure IAM roles have appropriate permissions  
- Validate DynamoDB access from ECS tasks  
