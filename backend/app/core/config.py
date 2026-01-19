from pathlib import Path

from pydantic_settings import BaseSettings
from typing import List


class Settings(BaseSettings):
    # Application
    APP_NAME: str = "Subscription Tracker API"
    APP_VERSION: str = "1.0.0"
    DEBUG: bool = True
    
    # Database
    DATABASE_URL: str = "sqlite+aiosqlite:///./subscriptions.db"

    # JWT Authentication
    SECRET_KEY: str = "your-secret-key-change-in-production"  # Change this!
    ALGORITHM: str = "HS256"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 30
    REFRESH_TOKEN_EXPIRE_DAYS: int = 7
    
    # Email (for verification)
    MAIL_USERNAME: str
    MAIL_PASSWORD: str
    MAIL_FROM: str
    MAIL_PORT: int
    MAIL_SERVER: str
    MAIL_FROM_NAME: str
    MAIL_STARTTLS: bool = True
    MAIL_SSL_TLS: bool = False
    USE_CREDENTIALS: bool = True
    VALIDATE_CERTS: bool = True
    
    # CORS - allowed origins for your Kotlin app
    CORS_ORIGINS: List[str] = ["*"]  # Restrict in production
    
    class Config:
        env_file = ".env"
        case_sensitive = True


settings = Settings()