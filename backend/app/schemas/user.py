from pydantic import BaseModel, EmailStr, Field
from typing import Optional
from datetime import datetime


# ============== Request Schemas ==============

class UserCreate(BaseModel):
    """Schema for user registration."""
    email: EmailStr
    password: str = Field(
        ...,
        min_length=8,
        max_length=72,
        description="Password must be 8-72 characters"
    )

class UserLogin(BaseModel):
    """Schema for user login."""
    email: EmailStr
    password: str


class UserUpdate(BaseModel):
    """Schema for updating user profile."""
    currency: Optional[str] = Field(None, min_length=3, max_length=3)
    country_code: Optional[str] = Field(None, min_length=2, max_length=2)
    notifications_enabled: Optional[bool] = None


class PasswordChange(BaseModel):
    """Schema for changing password."""
    current_password: str
    new_password: str = Field(..., min_length=8, max_length=72)


class EmailChange(BaseModel):
    """Schema for changing email."""
    new_email: EmailStr
    password: str  # Require password confirmation


# ============== Response Schemas ==============

class UserResponse(BaseModel):
    """Schema for user data in responses."""
    id: int
    email: str
    currency: str
    country_code: Optional[str]
    is_verified: bool
    notifications_enabled: bool
    created_at: datetime
    
    class Config:
        from_attributes = True  # Allows converting from SQLAlchemy model


class TokenResponse(BaseModel):
    """Schema for authentication tokens."""
    access_token: str
    refresh_token: str
    token_type: str = "bearer"


class MessageResponse(BaseModel):
    """Generic message response."""
    message: str
    success: bool = True
