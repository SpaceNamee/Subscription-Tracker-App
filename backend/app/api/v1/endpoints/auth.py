from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

from backend.app.db.database import get_db
from backend.app.models.user import User
from backend.app.schemas.mail import EmailModel
from backend.app.schemas.user import (
    UserCreate,
    UserLogin,
    UserResponse,
    TokenResponse,
    MessageResponse,
    PasswordChange,
    EmailChange,
)
from backend.app.core.security import (
    get_password_hash,
    verify_password,
    create_access_token,
    create_refresh_token,
    create_email_verification_token,
    verify_email_token,
    decode_token,
    get_current_user_id,
)

from backend.app.core.mail import create_message, mail

router = APIRouter(prefix="/auth", tags=["Authentication"])

@router.post('/send_mail')
async def send_mail(emails:EmailModel):
    emails = emails.addresses

    html = "<h1>Welcome to the app</h1>"

    message = create_message(
        recipients=emails,
        subject="Welcome",
        body=html
    )

    await mail.send_message(message)

    return {"message": "Email sent successfully."}

@router.post("/register", response_model=MessageResponse, status_code=status.HTTP_201_CREATED)
async def register(
    user_data: UserCreate,
    db: AsyncSession = Depends(get_db)
):
    """
    Register a new user.
    
    - Creates user account with hashed password
    - User needs to verify email before full access
    """
    # Check if email already exists
    result = await db.execute(select(User).where(User.email == user_data.email))
    existing_user = result.scalar_one_or_none()
    
    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Email already registered"
        )
    
    # Create new user
    new_user = User(
        email=user_data.email,
        hashed_password=get_password_hash(user_data.password),
        is_verified=False  # Requires email verification
    )
    
    db.add(new_user)
    await db.commit()
    
    # TODO: Send verification email
    # verification_token = create_email_verification_token(user_data.email)
    # await send_verification_email(user_data.email, verification_token)
    
    return MessageResponse(
        message="Registration successful. Please check your email to verify your account.",
        success=True
    )


@router.post("/login", response_model=TokenResponse)
async def login(
    credentials: UserLogin,
    db: AsyncSession = Depends(get_db)
):
    """
    Login with email and password.
    
    Returns access_token and refresh_token.
    """
    # Find user
    result = await db.execute(select(User).where(User.email == credentials.email))
    user = result.scalar_one_or_none()
    
    if not user or not verify_password(credentials.password, user.hashed_password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect email or password"
        )
    
    if not user.is_active:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Account is deactivated"
        )
    
    # Create tokens
    access_token = create_access_token(data={"sub": str(user.id)})
    refresh_token = create_refresh_token(data={"sub": str(user.id)})
    
    return TokenResponse(
        access_token=access_token,
        refresh_token=refresh_token
    )


@router.post("/refresh", response_model=TokenResponse)
async def refresh_token(
    refresh_token: str,
    db: AsyncSession = Depends(get_db)
):
    """
    Get new access token using refresh token.
    """
    payload = decode_token(refresh_token)
    
    if payload.get("type") != "refresh":
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid token type"
        )
    
    user_id = payload.get("sub")
    
    # Verify user still exists and is active
    result = await db.execute(select(User).where(User.id == int(user_id)))
    user = result.scalar_one_or_none()
    
    if not user or not user.is_active:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="User not found or inactive"
        )
    
    # Create new tokens
    new_access_token = create_access_token(data={"sub": str(user.id)})
    new_refresh_token = create_refresh_token(data={"sub": str(user.id)})
    
    return TokenResponse(
        access_token=new_access_token,
        refresh_token=new_refresh_token
    )


@router.post("/verify-email", response_model=MessageResponse)
async def verify_email(
    token: str,
    db: AsyncSession = Depends(get_db)
):
    """
    Verify user's email with token sent to their email.
    """
    email = verify_email_token(token)
    
    result = await db.execute(select(User).where(User.email == email))
    user = result.scalar_one_or_none()
    
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )
    
    user.is_verified = True
    await db.commit()
    
    return MessageResponse(message="Email verified successfully")


@router.get("/me", response_model=UserResponse)
async def get_current_user(
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Get current user's profile.
    
    Requires authentication (Bearer token).
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )
    
    return user


@router.post("/change-password", response_model=MessageResponse)
async def change_password(
    password_data: PasswordChange,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Change user's password.
    
    Requires current password for verification.
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    
    if not verify_password(password_data.current_password, user.hashed_password):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Current password is incorrect"
        )
    
    user.hashed_password = get_password_hash(password_data.new_password)
    await db.commit()
    
    return MessageResponse(message="Password changed successfully")


@router.post("/change-email", response_model=MessageResponse)
async def change_email(
    email_data: EmailChange,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Request email change.
    
    Sends verification to new email address.
    """
    result = await db.execute(select(User).where(User.id == user_id))
    user = result.scalar_one_or_none()
    
    # Verify password
    if not verify_password(email_data.password, user.hashed_password):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Password is incorrect"
        )
    
    # Check if new email is already taken
    result = await db.execute(select(User).where(User.email == email_data.new_email))
    if result.scalar_one_or_none():
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Email already in use"
        )
    
    # TODO: Send verification to new email
    # For now, just update directly (in production, verify first!)
    user.email = email_data.new_email
    user.is_verified = False  # Require re-verification
    await db.commit()
    
    return MessageResponse(message="Email updated. Please verify your new email address.")
