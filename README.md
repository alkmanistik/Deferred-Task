# Deferred Task Processor API

## Overview
REST API for scheduling and processing deferred tasks with configurable worker pools.

## Base URL
`http://localhost:8080/api/v1`

## Endpoints

### 1. Schedule a Task

**Endpoint**: `POST /tasks/schedule`

**Request**:
```json
{
    "category": "email",
    "taskClassName": "com.example.EmailProcessingTask",
    "taskParams": {
        "email": "user@example.com",
        "message": "Hello World"
    },
    "scheduledTime": "2025-06-10T15:30:00"
}
```
### 2. Start Worker
**Endpoint**: POST /workers/start
```
{
    "category": "email",
    "threadNumber": 3,
    "tasksNumber": 5,
    "retryCount": 3
}
```
### 3. Stop Worker
Endpoint: POST /workers/stop/{category}
