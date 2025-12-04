# Reminder API - Requirements Document

**Document Version:** 1.0  
**Date:** December 4, 2025  
**Prepared For:** API Testing Learning  
**API Base URL:** `http://localhost:8080`

---

## Table of Contents

1. [Overview](#overview)
2. [API Endpoints](#api-endpoints)
3. [Data Models](#data-models)
4. [Test Scenarios](#test-scenarios)
5. [Error Handling](#error-handling)
6. [Swagger Documentation](#swagger-documentation)
7. [Testing Checklist](#testing-checklist)

---

## Overview

### Purpose
This document describes the Reminder API, which provides RESTful endpoints for managing reminder records stored in the Companies House database. The API supports full CRUD (Create, Read, Update, Delete) operations with filtering and pagination capabilities.

### Base Information
- **Base URL:** `http://localhost:8080`
- **API Path Prefix:** `/api/reminders`
- **Content-Type:** `application/json`
- **Authentication:** None (for demo purposes)

### Key Features
- Retrieve all reminders with optional filtering
- Get a specific reminder by reference number
- Create new reminders
- Update existing reminders (full and partial updates)
- Delete reminders
- Filter by company number and status
- Pagination and sorting support

---

## API Endpoints

### 1. Get All Reminders

**Endpoint:** `GET /api/reminders`

**Description:** Retrieves a list of all reminders with optional filtering, pagination, and sorting.

**Request Parameters:**

| Parameter | Type | Required | Default | Description | Example |
|-----------|------|----------|---------|-------------|---------|
| `status` | String | No | - | Filter by reminder lifecycle status | `POSTED`, `SENT`, `DELIVERED` |
| `companyNumber` | String | No | - | Filter by company number | `15655987` |
| `page` | Integer | No | `0` | Page number (0-indexed) | `0`, `1`, `2` |
| `size` | Integer | No | `10` | Number of records per page | `10`, `20`, `50` |
| `sortBy` | String | No | `createdAt` | Field to sort by | `createdAt`, `dueDate`, `ref` |
| `sortDir` | String | No | `DESC` | Sort direction | `ASC`, `DESC` |

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Success - Returns list of reminders (may be empty if no filters applied) |
| `404 Not Found` | No reminders found when filters are applied |

**Example Request:**
```bash
# Get all reminders (first 10)
GET http://localhost:8080/api/reminders

# Get reminders with filters
GET http://localhost:8080/api/reminders?status=POSTED&companyNumber=15655987&page=0&size=20

# Get reminders with pagination
GET http://localhost:8080/api/reminders?page=0&size=5&sortBy=createdAt&sortDir=DESC
```

**Example Response (200 OK):**
```json
[
    {
        "ref": "26011715655987",
        "companyNumber": "15655987",
        "dueDate": "2026-01-17",
        "reminderLifecycleStatus": "POSTED",
        "reminderGeneratedAt": "2025-12-03T09:00:10.82",
        "reminderSentAt": null,
        "createdAt": "2025-12-03T09:00:10.86497",
        "updatedAt": "2025-12-03T09:00:16.16"
    },
    {
        "ref": "260117NI714223",
        "companyNumber": "NI714223",
        "dueDate": "2026-01-17",
        "reminderLifecycleStatus": "POSTED",
        "reminderGeneratedAt": "2025-12-03T09:00:10.822",
        "reminderSentAt": null,
        "createdAt": "2025-12-03T09:00:10.86497",
        "updatedAt": "2025-12-03T09:00:17.832"
    }
]
```

**Example Response (404 Not Found):**
```
Status: 404 Not Found
Body: (empty)
```

---

### 2. Get Reminder by Reference Number

**Endpoint:** `GET /api/reminders/{referenceNumber}`

**Description:** Retrieves a specific reminder using its reference number (ref).

**Path Parameters:**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `referenceNumber` | String | Yes | The reference number (ref) of the reminder | `26011715655987` |

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Success - Returns the reminder details |
| `404 Not Found` | Reminder not found with the given reference number |

**Example Request:**
```bash
GET http://localhost:8080/api/reminders/26011715655987
```

**Example Response (200 OK):**
```json
{
    "ref": "26011715655987",
    "companyNumber": "15655987",
    "dueDate": "2026-01-17",
    "reminderLifecycleStatus": "POSTED",
    "reminderGeneratedAt": "2025-12-03T09:00:10.82",
    "reminderSentAt": null,
    "createdAt": "2025-12-03T09:00:10.86497",
    "updatedAt": "2025-12-03T09:00:16.16"
}
```

**Example Response (404 Not Found):**
```json
{
    "timestamp": "2025-12-04T21:30:00.000+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Reminder not found with reference number: INVALID-REF",
    "path": "/api/reminders/INVALID-REF"
}
```

---

### 3. Create New Reminder

**Endpoint:** `POST /api/reminders`

**Description:** Creates a new reminder with the provided details.

**Request Body:**

| Field | Type | Required | Description | Example |
|-------|------|----------|-------------|---------|
| `ref` | String | Yes | Reference number (must be unique) | `26011715655987` |
| `companyNumber` | String | Yes | Company number | `15655987` |
| `dueDate` | Date (YYYY-MM-DD) | Yes | Due date for the reminder | `2026-01-17` |
| `reminderLifecycleStatus` | String | No | Reminder lifecycle status | `POSTED`, `SENT`, `DELIVERED`, `PENDING` |

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| `201 Created` | Success - Reminder created successfully |
| `400 Bad Request` | Invalid input or duplicate reference number |

**Example Request:**
```bash
POST http://localhost:8080/api/reminders
Content-Type: application/json

{
    "ref": "TEST20241204001",
    "companyNumber": "12345678",
    "dueDate": "2026-12-31",
    "reminderLifecycleStatus": "PENDING"
}
```

**Example Response (201 Created):**
```json
{
    "ref": "TEST20241204001",
    "companyNumber": "12345678",
    "dueDate": "2026-12-31",
    "reminderLifecycleStatus": "PENDING",
    "reminderGeneratedAt": "2025-12-04T21:30:00.000",
    "reminderSentAt": null,
    "createdAt": "2025-12-04T21:30:00.000",
    "updatedAt": "2025-12-04T21:30:00.000"
}
```

**Example Response (400 Bad Request):**
```json
{
    "timestamp": "2025-12-04T21:30:00.000+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Reminder with reference number already exists: TEST20241204001",
    "path": "/api/reminders"
}
```

---

### 4. Update Reminder (Full Update)

**Endpoint:** `PUT /api/reminders/{referenceNumber}`

**Description:** Updates an existing reminder identified by reference number. All fields must be provided (full update).

**Path Parameters:**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `referenceNumber` | String | Yes | The reference number of the reminder to update | `26011715655987` |

**Request Body:** Same as Create (all fields required)

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Success - Reminder updated successfully |
| `404 Not Found` | Reminder not found with the given reference number |
| `400 Bad Request` | Invalid input or duplicate reference number |

**Example Request:**
```bash
PUT http://localhost:8080/api/reminders/26011715655987
Content-Type: application/json

{
    "ref": "26011715655987",
    "companyNumber": "15655987",
    "dueDate": "2026-02-17",
    "reminderLifecycleStatus": "SENT"
}
```

**Example Response (200 OK):**
```json
{
    "ref": "26011715655987",
    "companyNumber": "15655987",
    "dueDate": "2026-02-17",
    "reminderLifecycleStatus": "SENT",
    "reminderGeneratedAt": "2025-12-03T09:00:10.82",
    "reminderSentAt": null,
    "createdAt": "2025-12-03T09:00:10.86497",
    "updatedAt": "2025-12-04T21:35:00.000"
}
```

---

### 5. Partial Update Reminder

**Endpoint:** `PATCH /api/reminders/{referenceNumber}`

**Description:** Partially updates an existing reminder. Only provided fields will be updated.

**Path Parameters:**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `referenceNumber` | String | Yes | The reference number of the reminder to update | `26011715655987` |

**Request Body:** Any combination of fields (all optional)

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Success - Reminder updated successfully |
| `404 Not Found` | Reminder not found with the given reference number |
| `400 Bad Request` | Invalid input |

**Example Request:**
```bash
PATCH http://localhost:8080/api/reminders/26011715655987
Content-Type: application/json

{
    "reminderLifecycleStatus": "DELIVERED"
}
```

**Example Response (200 OK):**
```json
{
    "ref": "26011715655987",
    "companyNumber": "15655987",
    "dueDate": "2026-01-17",
    "reminderLifecycleStatus": "DELIVERED",
    "reminderGeneratedAt": "2025-12-03T09:00:10.82",
    "reminderSentAt": null,
    "createdAt": "2025-12-03T09:00:10.86497",
    "updatedAt": "2025-12-04T21:40:00.000"
}
```

---

### 6. Delete Reminder

**Endpoint:** `DELETE /api/reminders/{referenceNumber}`

**Description:** Deletes a reminder identified by reference number.

**Path Parameters:**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `referenceNumber` | String | Yes | The reference number of the reminder to delete | `26011715655987` |

**Response Codes:**

| Status Code | Description |
|-------------|-------------|
| `204 No Content` | Success - Reminder deleted successfully |
| `404 Not Found` | Reminder not found with the given reference number |

**Example Request:**
```bash
DELETE http://localhost:8080/api/reminders/TEST20241204001
```

**Example Response (204 No Content):**
```
Status: 204 No Content
Body: (empty)
```

---

## Data Models

### ReminderRequest

Used for creating and updating reminders.

```json
{
    "ref": "string (required, unique)",
    "companyNumber": "string (required)",
    "dueDate": "date (required, format: YYYY-MM-DD)",
    "reminderLifecycleStatus": "string (optional)"
}
```

**Field Details:**

- **ref**: Reference number, must be unique across all reminders
- **companyNumber**: Company identifier (e.g., `15655987`, `NI714223`)
- **dueDate**: Due date in ISO date format (YYYY-MM-DD)
- **reminderLifecycleStatus**: Status values: `POSTED`, `SENT`, `DELIVERED`, `PENDING`

### ReminderResponse

Returned by all GET endpoints and after create/update operations.

```json
{
    "ref": "string",
    "companyNumber": "string",
    "dueDate": "date (YYYY-MM-DD)",
    "reminderLifecycleStatus": "string",
    "reminderGeneratedAt": "datetime (ISO 8601)",
    "reminderSentAt": "datetime (ISO 8601) or null",
    "createdAt": "datetime (ISO 8601)",
    "updatedAt": "datetime (ISO 8601)"
}
```

**Field Details:**

- **ref**: Reference number (primary key)
- **companyNumber**: Company identifier
- **dueDate**: Due date
- **reminderLifecycleStatus**: Current status
- **reminderGeneratedAt**: When the reminder was generated (auto-set on create)
- **reminderSentAt**: When the reminder was sent (nullable)
- **createdAt**: Record creation timestamp (auto-set)
- **updatedAt**: Last update timestamp (auto-updated)

---

## Test Scenarios

### Scenario 1: Retrieve All Reminders

**Objective:** Verify that the API returns all reminders without filters.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders`
2. Verify response status is `200 OK`
3. Verify response contains an array of reminders
4. Verify each reminder has required fields: `ref`, `companyNumber`, `dueDate`, `reminderLifecycleStatus`

**Expected Result:** Returns list of reminders (may be empty if database is empty)

---

### Scenario 2: Filter by Company Number (Existing)

**Objective:** Verify filtering by company number returns correct results.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders?companyNumber=15655987`
2. Verify response status is `200 OK`
3. Verify all returned reminders have `companyNumber` = `15655987`

**Expected Result:** Returns only reminders for the specified company number

---

### Scenario 3: Filter by Company Number (Non-Existent)

**Objective:** Verify that filtering by non-existent company number returns 404.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders?companyNumber=99999999`
2. Verify response status is `404 Not Found`

**Expected Result:** Returns 404 status code

---

### Scenario 4: Filter by Status

**Objective:** Verify filtering by status works correctly.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders?status=POSTED`
2. Verify response status is `200 OK`
3. Verify all returned reminders have `reminderLifecycleStatus` = `POSTED`

**Expected Result:** Returns only reminders with the specified status

---

### Scenario 5: Pagination

**Objective:** Verify pagination works correctly.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders?page=0&size=5`
2. Verify response status is `200 OK`
3. Verify response contains maximum 5 records
4. Send GET request with `page=1&size=5`
5. Verify different records are returned (if more than 5 exist)

**Expected Result:** Returns correct number of records per page

---

### Scenario 6: Sorting

**Objective:** Verify sorting works correctly.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders?sortBy=createdAt&sortDir=DESC`
2. Verify response status is `200 OK`
3. Verify records are sorted by `createdAt` in descending order
4. Send GET request with `sortDir=ASC`
5. Verify records are sorted in ascending order

**Expected Result:** Returns records in the specified sort order

---

### Scenario 7: Get Reminder by Reference Number (Existing)

**Objective:** Verify retrieving a specific reminder works.

**Steps:**
1. First, get a reference number from the list endpoint
2. Send GET request to `http://localhost:8080/api/reminders/{ref}`
3. Verify response status is `200 OK`
4. Verify response contains the correct reminder details

**Expected Result:** Returns the specific reminder

---

### Scenario 8: Get Reminder by Reference Number (Non-Existent)

**Objective:** Verify that requesting non-existent reminder returns 404.

**Steps:**
1. Send GET request to `http://localhost:8080/api/reminders/INVALID-REF-12345`
2. Verify response status is `404 Not Found`

**Expected Result:** Returns 404 status code

---

### Scenario 9: Create New Reminder

**Objective:** Verify creating a new reminder works correctly.

**Steps:**
1. Prepare a unique reference number (e.g., `TEST-{timestamp}`)
2. Send POST request to `http://localhost:8080/api/reminders` with valid data
3. Verify response status is `201 Created`
4. Verify response contains the created reminder with all fields
5. Verify `reminderGeneratedAt` and `createdAt` are set

**Expected Result:** Reminder is created successfully

---

### Scenario 10: Create Duplicate Reminder

**Objective:** Verify that creating a reminder with duplicate reference number fails.

**Steps:**
1. Create a reminder with reference number `TEST-DUP-001`
2. Try to create another reminder with the same reference number
3. Verify response status is `400 Bad Request`
4. Verify error message indicates duplicate reference number

**Expected Result:** Returns 400 error with appropriate message

---

### Scenario 11: Update Reminder (Full Update)

**Objective:** Verify full update works correctly.

**Steps:**
1. Get an existing reminder reference number
2. Send PUT request with updated data
3. Verify response status is `200 OK`
4. Verify response contains updated data
5. Verify `updatedAt` timestamp is changed
6. Get the reminder again and verify changes persisted

**Expected Result:** Reminder is updated successfully

---

### Scenario 12: Partial Update Reminder

**Objective:** Verify partial update works correctly.

**Steps:**
1. Get an existing reminder reference number
2. Note the current `reminderLifecycleStatus`
3. Send PATCH request with only `reminderLifecycleStatus` field updated
4. Verify response status is `200 OK`
5. Verify only the specified field is updated
6. Verify other fields remain unchanged

**Expected Result:** Only specified fields are updated

---

### Scenario 13: Delete Reminder

**Objective:** Verify deleting a reminder works correctly.

**Steps:**
1. Create a test reminder
2. Note the reference number
3. Send DELETE request to `http://localhost:8080/api/reminders/{ref}`
4. Verify response status is `204 No Content`
5. Try to get the deleted reminder
6. Verify it returns `404 Not Found`

**Expected Result:** Reminder is deleted successfully

---

### Scenario 14: Delete Non-Existent Reminder

**Objective:** Verify that deleting non-existent reminder returns 404.

**Steps:**
1. Send DELETE request to `http://localhost:8080/api/reminders/INVALID-REF-99999`
2. Verify response status is `404 Not Found`

**Expected Result:** Returns 404 status code

---

### Scenario 15: Combined Filters

**Objective:** Verify multiple filters work together.

**Steps:**
1. Send GET request with both `status` and `companyNumber` filters
2. Verify response status is `200 OK`
3. Verify all returned reminders match both criteria

**Expected Result:** Returns reminders matching all specified filters

---

## Error Handling

### Common Error Responses

#### 400 Bad Request
**When:** Invalid input data, missing required fields, or duplicate reference number

**Example:**
```json
{
    "timestamp": "2025-12-04T21:30:00.000+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Reminder with reference number already exists: TEST-001",
    "path": "/api/reminders"
}
```

#### 404 Not Found
**When:** 
- Reminder not found by reference number
- No reminders found when filters are applied

**Example:**
```json
{
    "timestamp": "2025-12-04T21:30:00.000+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Reminder not found with reference number: INVALID-REF",
    "path": "/api/reminders/INVALID-REF"
}
```

#### 500 Internal Server Error
**When:** Unexpected server error occurs

**Example:**
```json
{
    "timestamp": "2025-12-04T21:30:00.000+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "An unexpected error occurred",
    "path": "/api/reminders"
}
```

---

## Swagger Documentation

### Accessing Swagger UI

1. **Open your web browser**
2. **Navigate to:** `http://localhost:8080/swagger-ui.html`
3. **You will see:** Interactive API documentation with all endpoints

### Using Swagger UI for Testing

1. **Select an endpoint** from the list (e.g., "GET /api/reminders")
2. **Click "Try it out"** button
3. **Enter parameters** (if any) in the input fields
4. **Click "Execute"** button
5. **View the response** below with status code and body

### Benefits of Using Swagger

- **No need to write curl commands** - UI handles it
- **See all available parameters** and their descriptions
- **Test requests directly** from the browser
- **View request/response examples**
- **Understand data models** and field requirements

---

## Testing Checklist

Use this checklist to ensure comprehensive testing:

### GET Endpoints

- [ ] Get all reminders (no filters)
- [ ] Get all reminders with pagination (page=0, size=5)
- [ ] Get all reminders with status filter (existing status)
- [ ] Get all reminders with status filter (non-existent status) → should return 404
- [ ] Get all reminders with companyNumber filter (existing)
- [ ] Get all reminders with companyNumber filter (non-existent) → should return 404
- [ ] Get all reminders with multiple filters
- [ ] Get all reminders with sorting (ASC)
- [ ] Get all reminders with sorting (DESC)
- [ ] Get reminder by reference number (existing)
- [ ] Get reminder by reference number (non-existent) → should return 404

### POST Endpoint

- [ ] Create reminder with all required fields
- [ ] Create reminder with optional fields
- [ ] Create reminder with duplicate reference number → should return 400
- [ ] Create reminder with missing required field → should return 400
- [ ] Create reminder with invalid date format → should return 400

### PUT Endpoint

- [ ] Update reminder with all fields (existing reminder)
- [ ] Update reminder with non-existent reference number → should return 404
- [ ] Update reminder with duplicate reference number → should return 400

### PATCH Endpoint

- [ ] Partial update - update only status field
- [ ] Partial update - update only dueDate field
- [ ] Partial update - update multiple fields
- [ ] Partial update with non-existent reference number → should return 404

### DELETE Endpoint

- [ ] Delete existing reminder → should return 204
- [ ] Delete non-existent reminder → should return 404
- [ ] Verify deleted reminder cannot be retrieved

### Edge Cases

- [ ] Test with empty request body
- [ ] Test with invalid JSON format
- [ ] Test with very large page size
- [ ] Test with negative page number
- [ ] Test with special characters in company number
- [ ] Test with very long reference number

---

## Sample Test Data

### Valid Test Reminders

```json
{
    "ref": "TEST-2024-001",
    "companyNumber": "12345678",
    "dueDate": "2026-12-31",
    "reminderLifecycleStatus": "PENDING"
}
```

```json
{
    "ref": "TEST-2024-002",
    "companyNumber": "87654321",
    "dueDate": "2026-06-15",
    "reminderLifecycleStatus": "POSTED"
}
```

### Reference Numbers for Testing

- **Existing (from database):** `26011715655987`, `260117NI714223`, `26011715655420`
- **Non-existent:** `INVALID-REF-99999`, `TEST-NOT-FOUND-001`

### Company Numbers for Testing

- **Existing:** `15655987`, `NI714223`, `15655420`
- **Non-existent:** `99999999`, `00000000`

---

## Tools for API Testing

### 1. Swagger UI (Recommended for Beginners)
- **URL:** `http://localhost:8080/swagger-ui.html`
- **Best for:** Visual testing, understanding API structure
- **No installation required**

### 2. Postman
- **Download:** https://www.postman.com/downloads/
- **Best for:** Advanced testing, test collections, automation
- **Features:** Save requests, create test suites, environment variables

### 3. cURL (Command Line)
- **Best for:** Quick tests, scripting, CI/CD
- **Example:**
  ```bash
  curl -X GET "http://localhost:8080/api/reminders" -H "accept: application/json"
  ```

### 4. Browser
- **Best for:** Simple GET requests
- **Just type URL in address bar:** `http://localhost:8080/api/reminders`

---

## Tips for Manual Testing

1. **Start with Swagger UI** - It's the easiest way to understand the API
2. **Test happy paths first** - Verify basic functionality works
3. **Then test error cases** - Verify error handling works correctly
4. **Use real data from database** - Get actual reference numbers from GET all endpoint
5. **Clean up test data** - Delete test reminders after testing
6. **Document your findings** - Note any issues or unexpected behavior
7. **Test one thing at a time** - Don't combine multiple test scenarios
8. **Verify response structure** - Check that all expected fields are present
9. **Check status codes** - Ensure correct HTTP status codes are returned
10. **Test edge cases** - Try invalid inputs, empty values, etc.

---

## Support and Questions

If you encounter any issues or have questions:

1. **Check Swagger Documentation** - Most answers are in the Swagger UI
2. **Review this document** - All endpoints and scenarios are documented
3. **Check response error messages** - They often explain what went wrong
4. **Verify application is running** - Ensure `http://localhost:8080` is accessible

---

**Document End**

*Happy Testing! 🚀*

