# 📱 Subscription Tracker API

FastAPI backend for the Mobile Subscription Tracker application.

## 🚀 Quick Start

### Prerequisites
- Python 3.10+
- pip

### Installation

```bash
# 1. Clone the repository
git clone <your-repo-url>
cd subscription-tracker-api/backend

# 2. Create virtual environment
python -m venv venv

# On Windows:
venv\Scripts\activate

# On macOS/Linux:
source venv/bin/activate

# 3. Install dependencies
pip install -r requirements.txt

# 4. Edit environment file example with your data

# 5. Run the server
uvicorn app.main:app --reload --port 8001
```

---

## 📖 API Documentation for Kotlin Team

### Base URL
```
SOON
```

### Authentication

All protected endpoints require a Bearer token in the header:
```
Authorization: Bearer <access_token>
```

---

## 🔐 Auth Endpoints

### Register
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "securepassword123"
}
```

**Response (201):**
```json
{
    "message": "Registration successful. Please check your email to verify your account.",
    "success": true
}
```

---

### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "securepassword123"
}
```

**Response (200):**
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
    "token_type": "bearer"
}
```

---

### Refresh Token
```http
POST /api/v1/auth/refresh?refresh_token=<your_refresh_token>
```

---

### Get Current User
```http
GET /api/v1/auth/me
Authorization: Bearer <access_token>
```

**Response (200):**
```json
{
    "id": 1,
    "email": "user@example.com",
    "currency": "USD",
    "country_code": null,
    "is_verified": false,
    "notifications_enabled": true,
    "created_at": "2024-01-15T10:30:00Z"
}
```

---

## 📋 Subscription Endpoints

### Get Popular Subscriptions (Templates)
```http
GET /api/v1/subscriptions/popular
```

**Response (200):**
```json
[
    {
        "name": "Netflix",
        "category": "streaming",
        "logo_url": "https://logo.clearbit.com/netflix.com",
        "website_url": "https://www.netflix.com/account",
        "suggested_amount": 15.49,
        "suggested_period": "monthly"
    },
    ...
]
```

---

### Get Categories
```http
GET /api/v1/subscriptions/categories
```

**Response (200):**
```json
["streaming", "music", "gaming", "productivity", "cloud_storage", ...]
```

---

### Create Subscription
```http
POST /api/v1/subscriptions
Authorization: Bearer <access_token>
Content-Type: application/json

{
    "name": "Netflix",
    "category": "streaming",
    "amount": 15.49,
    "currency": "USD",
    "payment_period": "monthly",
    "first_payment_date": "2024-01-01T00:00:00Z",
    "website_url": "https://www.netflix.com/account",
    "logo_url": "https://logo.clearbit.com/netflix.com"
}
```

**Payment Periods:** `weekly`, `monthly`, `quarterly`, `yearly`

---

### Get All Subscriptions
```http
GET /api/v1/subscriptions
Authorization: Bearer <access_token>

# Optional filters:
GET /api/v1/subscriptions?category=streaming
GET /api/v1/subscriptions?is_active=true
```

**Response (200):**
```json
{
    "subscriptions": [
        {
            "id": 1,
            "name": "Netflix",
            "description": null,
            "category": "streaming",
            "amount": 15.49,
            "currency": "USD",
            "payment_period": "monthly",
            "first_payment_date": "2024-01-01T00:00:00Z",
            "next_payment_date": "2024-02-01T00:00:00Z",
            "website_url": "https://www.netflix.com/account",
            "logo_url": "https://logo.clearbit.com/netflix.com",
            "is_active": true,
            "created_at": "2024-01-15T10:30:00Z"
        }
    ],
    "total": 1
}
```

---

### Update Subscription
```http
PUT /api/v1/subscriptions/{subscription_id}
Authorization: Bearer <access_token>
Content-Type: application/json

{
    "amount": 17.99
}
```

---

### Delete Subscription
```http
DELETE /api/v1/subscriptions/{subscription_id}
Authorization: Bearer <access_token>
```

---

### Toggle Active/Inactive
```http
POST /api/v1/subscriptions/{subscription_id}/toggle
Authorization: Bearer <access_token>
```

---

## 📊 Analytics Endpoints

### Get Spending Analytics
```http
GET /api/v1/analytics/spending?period=month
Authorization: Bearer <access_token>

# period: "month" or "year"
```

**Response (200):**
```json
{
    "period": "month",
    "start_date": "2024-01-15T00:00:00Z",
    "end_date": "2024-02-15T00:00:00Z",
    "total_amount": 45.47,
    "currency": "USD",
    "by_category": [
        {
            "category": "streaming",
            "total_amount": 29.48,
            "currency": "USD",
            "subscription_count": 2,
            "percentage": 64.8
        }
    ],
    "subscription_count": 3
}
```

---
### Get Payment History by Category
```http
GET /api/v1/analytics/spending/category/{category}?period=month
Authorization: Bearer <access_token>
```

**Response (200):**
```json
{
    "category": "streaming",
    "period": "month",
    "start_date": "2025-01-01T00:00:00",
    "end_date": "2025-01-31T23:59:59",
    "total_amount": 29.48,
    "payment_count": 2,
    "payments": [
        {
            "subscription_id": 1,
            "subscription_name": "Netflix",
            "category": "streaming",
            "amount": 15.49,
            "currency": "USD",
            "payment_date": "2025-01-15T00:00:00",
            "logo_url": "..."
        }
    ]
}
```

### Get Full Payment History
```http
GET /api/v1/analytics/history?period=month
Authorization: Bearer <access_token>
```

**Response (200):**
```json
{
    "period": "month",
    "start_date": "2025-01-01T00:00:00",
    "end_date": "2025-01-31T23:59:59",
    "total_amount": 85.47,
    "payment_count": 5,
    "currency": "USD",
    "payments": [
        {
            "subscription_id": 1,
            "subscription_name": "Netflix",
            "category": "streaming",
            "amount": 15.49,
            "payment_date": "2025-01-15T00:00:00",
            ...
        }
    ]
}
```

### Get Subscriptions by Category
```http
GET /api/v1/analytics/spending/category/streaming
Authorization: Bearer <access_token>
```

---

### Get Upcoming Payments
```http
GET /api/v1/analytics/upcoming?days=30
Authorization: Bearer <access_token>
```

**Response (200):**
```json
{
    "payments": [
        {
            "subscription_id": 1,
            "subscription_name": "Netflix",
            "amount": 15.49,
            "currency": "USD",
            "payment_date": "2024-02-01T00:00:00Z",
            "days_until_payment": 5
        }
    ],
    "total_upcoming_amount": 15.49,
    "currency": "USD"
}
```

---

### Quick Summary (Dashboard)
```http
GET /api/v1/analytics/summary
Authorization: Bearer <access_token>
```

**Response (200):**
```json
{
    "active_subscriptions": 5,
    "monthly_total": 67.45,
    "yearly_total": 809.40,
    "currency": "USD",
    "next_payment": {
        "subscription_name": "Netflix",
        "amount": 15.49,
        "currency": "USD",
        "date": "2024-02-01T00:00:00Z",
        "days_until": 5
    }
}
```

---

## ⚙️ User Settings Endpoints

### Update Profile
```http
PATCH /api/v1/users/me
Authorization: Bearer <access_token>
Content-Type: application/json

{
    "currency": "EUR",
    "notifications_enabled": false
}
```

---

### Get Supported Currencies
```http
GET /api/v1/users/currencies
```

---

### Change Password
```http
POST /api/v1/auth/change-password
Authorization: Bearer <access_token>
Content-Type: application/json

{
    "current_password": "oldpassword123",
    "new_password": "newpassword456"
}
```

---

### Change Email
```http
POST /api/v1/auth/change-email
Authorization: Bearer <access_token>
Content-Type: application/json

{
    "new_email": "newemail@example.com",
    "password": "currentpassword123"
}
```

---

## 🔧 Kotlin Integration Example

```kotlin
// Using Retrofit

interface SubscriptionApi {
    
    @POST("auth/login")
    suspend fun login(@Body credentials: LoginRequest): TokenResponse
    
    @GET("subscriptions")
    suspend fun getSubscriptions(
        @Header("Authorization") token: String
    ): SubscriptionListResponse
    
    @POST("subscriptions")
    suspend fun createSubscription(
        @Header("Authorization") token: String,
        @Body subscription: SubscriptionCreate
    ): SubscriptionResponse
    
    @GET("analytics/summary")
    suspend fun getSummary(
        @Header("Authorization") token: String
    ): SummaryResponse
}

// Usage
val token = "Bearer ${loginResponse.access_token}"
val subscriptions = api.getSubscriptions(token)
```

---

## 📁 Project Structure

```
subscription-tracker-api/
├── app/
│   ├── api/
│   │   └── v1/
│   │       ├── endpoints/
│   │       │   ├── auth.py
│   │       │   ├── subscriptions.py
│   │       │   ├── analytics.py
│   │       │   └── users.py
│   │       └── router.py
│   ├── core/
│   │   ├── config.py
│   │   └── security.py
│   ├── db/
│   │   └── database.py
│   ├── models/
│   │   ├── user.py
│   │   └── subscription.py
│   ├── schemas/
│   │   ├── user.py
│   │   ├── subscription.py
│   │   └── analytics.py
│   └── main.py
├── tests/
├── requirements.txt
├── .env.example
└── README.md
```

---

## 🚀 Deployment

### Option 1: Railway (Recommended for beginners)
1. Push code to GitHub
2. Connect Railway to your repo
3. Add environment variables
4. Deploy!

### Option 2: Render
1. Create new Web Service
2. Connect GitHub repo
3. Set build command: `pip install -r requirements.txt`
4. Set start command: `uvicorn app.main:app --host 0.0.0.0 --port $PORT`

---

## ❓ Questions?

If you have any questions about the API:
1. Check the interactive docs at `/docs`
2. Test endpoints directly in Swagger UI
3. Contact the backend developer

Happy coding! 🎉
