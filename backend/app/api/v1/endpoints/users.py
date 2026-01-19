from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

from backend.app.db.database import get_db
from backend.app.models.user import User
from backend.app.schemas.user import UserUpdate, UserResponse, MessageResponse
from backend.app.core.security import get_current_user_id

router = APIRouter(prefix="/users", tags=["User Settings"])


@router.get("/me", response_model=UserResponse)
async def get_profile(
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Get current user's profile.
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )
    
    return user


@router.patch("/me", response_model=UserResponse)
async def update_profile(
    update_data: UserUpdate,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Update user settings.
    
    - currency: Change preferred currency (e.g., "USD", "EUR", "PLN")
    - country_code: Update country code (e.g., "US", "PL")
    - notifications_enabled: Turn notifications on/off
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )
    
    # Update only provided fields
    update_dict = update_data.model_dump(exclude_unset=True)
    
    for field, value in update_dict.items():
        setattr(user, field, value)
    
    await db.commit()
    await db.refresh(user)
    
    return user


@router.delete("/me", response_model=MessageResponse)
async def delete_account(
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Delete user account and all associated data.
    
    This action is irreversible!
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )
    
    await db.delete(user)
    await db.commit()
    
    return MessageResponse(message="Account deleted successfully")


# Currency list for the app
SUPPORTED_CURRENCIES = [
    {"code": "USD", "name": "US Dollar", "symbol": "$"},
    {"code": "EUR", "name": "Euro", "symbol": "€"},
    {"code": "GBP", "name": "British Pound", "symbol": "£"},
    {"code": "PLN", "name": "Polish Złoty", "symbol": "zł"},
    {"code": "UAH", "name": "Ukrainian Hryvnia", "symbol": "₴"},
    {"code": "JPY", "name": "Japanese Yen", "symbol": "¥"},
    {"code": "CAD", "name": "Canadian Dollar", "symbol": "C$"},
    {"code": "AUD", "name": "Australian Dollar", "symbol": "A$"},
    {"code": "CHF", "name": "Swiss Franc", "symbol": "CHF"},
    {"code": "CNY", "name": "Chinese Yuan", "symbol": "¥"},
    {"code": "INR", "name": "Indian Rupee", "symbol": "₹"},
    {"code": "BRL", "name": "Brazilian Real", "symbol": "R$"},
]


@router.get("/currencies")
async def get_supported_currencies():
    """
    Get list of supported currencies.
    
    No authentication required.
    """
    return SUPPORTED_CURRENCIES
