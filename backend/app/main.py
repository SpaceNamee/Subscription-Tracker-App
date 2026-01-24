from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.core.config import settings
from app.db.database import init_db
from app.api.v1.router import api_router


@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Lifecycle manager for the application.
    - Runs on startup: Initialize database
    - Runs on shutdown: Cleanup (if needed)
    """
    # Startup
    print("🚀 Starting up...")
    await init_db()
    print("✅ Database initialized")
    
    yield  # Application runs here
    
    # Shutdown
    print("👋 Shutting down...")


# Create FastAPI application
app = FastAPI(
    title=settings.APP_NAME,
    version=settings.APP_VERSION,
    description="""
    ## Mobile Subscription Tracker API
    
    Backend API for tracking and managing subscriptions.
    
    ### Features:
    - 🔐 **Authentication**: Register, login, email verification
    - 📱 **Subscriptions**: Create, read, update, delete subscriptions
    - 📊 **Analytics**: Spending breakdowns by category and time period
    - ⚙️ **Settings**: User preferences, currency, notifications
    
    ### Authentication:
    Most endpoints require a Bearer token. Get one by:
    1. Register at `/api/v1/auth/register`
    2. Login at `/api/v1/auth/login`
    3. Use the `access_token` in the Authorization header
    
    Example: `Authorization: Bearer your_token_here`
    """,
    lifespan=lifespan,
    docs_url="/docs",      # Swagger UI
    redoc_url="/redoc",    # ReDoc UI
)

# Configure CORS - allows Kotlin app to make requests
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,  # ["*"] allows all origins
    allow_credentials=True,
    allow_methods=["*"],    # Allow all HTTP methods
    allow_headers=["*"],    # Allow all headers
)

# Include API routes
app.include_router(api_router)


# Root endpoint - health check
@app.get("/", tags=["Health"])
async def root():
    """
    Health check endpoint.
    
    Returns API status and version.
    """
    return {
        "status": "healthy",
        "app": settings.APP_NAME,
        "version": settings.APP_VERSION,
        "docs": "/docs"
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """
    Detailed health check.
    """
    return {
        "status": "healthy",
        "database": "connected",
        "version": settings.APP_VERSION
    }


# Run with: uvicorn app.main:app --reload
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="0.0.0.0", port=8001, reload=True)
