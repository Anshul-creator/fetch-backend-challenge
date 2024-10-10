# Fetch Backend Internship Challenge

## Overview

This project is a solution to the Fetch Backend Internship Challenge. The project implements a REST API to track and spend points from multiple payers based on the First In, First Out (FIFO) principle. The API allows users to:
- Add transactions (points from payers).
- Spend points using the FIFO rule.
- Retrieve the current balance of points for each payer.

## Tools & Technologies

- **Java 17**
- **Spring Boot**
- **Gradle** for build automation
- **Lombok** to reduce boilerplate code
- **Postman** or **curl** for API testing

## Prerequisites

- **Java 17** must be installed and set as the default JVM.
- **Gradle** is used for building the project.
- **Postman** or **curl** for testing the API endpoints.

## Setup Instructions

1. Clone this repository:
   git clone https://github.com/Anshul-creator/fetch-backend-challenge

2. Build and run the application:
   - ./gradlew bootRun
   - This will start the application on http://localhost:8000.

## API Endpoints

### 1. Add Transaction
**POST** `/add`

This endpoint allows you to add a transaction for a payer with the specified points and timestamp.

#### Request Body (JSON):
{
  "payer": "DANNON",
  "points": 300,
  "timestamp": "2022-10-31T10:00:00Z"
}

#### Response:
- Status: `200 OK` on success.

### 2. Spend Points

**POST** `/spend`

This endpoint allows you to spend points using the FIFO approach. You provide the number of points you want to spend, and the response shows how those points were deducted from different payers.

#### Request Body (JSON):
{
  "points": 5000
}

#### Response:
- Status: `200 OK` on success, `400 Bad Request` if amount to be spent is more than user points.
- Example Response:
[
  { "payer": "DANNON", "points": -100 },
  { "payer": "UNILEVER", "points": -200 },
  { "payer": "MILLER COORS", "points": -4700 }
]

### Check Balance

**GET** `/balance`

This endpoint returns the current balance of points for each payer.

#### Response:
- Status: `200 OK` on success.
- Example Response:
{
  "DANNON": 1000,
  "UNILEVER": 0,
  "MILLER COORS": 5300
}