from anyio.from_thread import run_sync
from fastapi import APIRouter, Depends, HTTPException, status, Query
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, and_
from typing import List, Optional
from datetime import datetime, timedelta, timezone
from dateutil.relativedelta import relativedelta

from app.db.database import get_db
from app.models.subscription import Subscription, PaymentPeriod, SubscriptionCategory
from app.schemas.subscription import (
    SubscriptionCreate,
    SubscriptionUpdate,
    SubscriptionResponse,
    SubscriptionListResponse,
    PopularSubscription,
    POPULAR_SUBSCRIPTIONS,
)
from app.schemas.user import MessageResponse
from app.core.security import get_current_user_id

router = APIRouter(prefix="/subscriptions", tags=["Subscriptions"])


def calculate_next_payment_date(first_payment: datetime, period: PaymentPeriod) -> datetime:
    """Calculate next payment date based on first payment and period."""
    now = datetime.now(timezone.utc)
    # If first_payment is naive, make it aware
    if first_payment.tzinfo is None:
        first_payment = first_payment.replace(tzinfo=timezone.utc)

    next_date = first_payment
    
    while next_date <= now:
        if period == PaymentPeriod.WEEKLY:
            next_date += timedelta(weeks=1)
        elif period == PaymentPeriod.MONTHLY:
            next_date += relativedelta(months=1)
        elif period == PaymentPeriod.QUARTERLY:
            next_date += relativedelta(months=3)
        elif period == PaymentPeriod.YEARLY:
            next_date += relativedelta(years=1)
    
    return next_date


@router.get("/popular", response_model=List[PopularSubscription])
async def get_popular_subscriptions():
    """
    Get list of popular subscription templates.
    
    These are pre-defined subscriptions that users can quickly add.
    No authentication required.
    """
    return POPULAR_SUBSCRIPTIONS


@router.get("/categories", response_model=List[str])
async def get_categories():
    """
    Get all available subscription categories.
    
    No authentication required.
    """
    return [category.value for category in SubscriptionCategory]


@router.post("", response_model=SubscriptionResponse, status_code=status.HTTP_201_CREATED)
async def create_subscription(
    subscription_data: SubscriptionCreate,
    db: AsyncSession = Depends(get_db),
    user_id: int = Depends(get_current_user_id)
):
    """Create a new subscription ignoring auth for testing."""
    new_subscription = Subscription(
        user_id=user_id,
        # ... outros campos
    )
    
    # O resto do código continua igual...
    next_payment = calculate_next_payment_date(
        subscription_data.first_payment_date,
        subscription_data.payment_period
    )
    
    new_subscription = Subscription(
        user_id=user_id,
        name=subscription_data.name,
        description=subscription_data.description,
        category=subscription_data.category,
        amount=subscription_data.amount,
        currency=subscription_data.currency,
        payment_period=subscription_data.payment_period,
        first_payment_date=subscription_data.first_payment_date,
        website_url=subscription_data.website_url,
        logo_url=subscription_data.logo_url,
    )
    
    db.add(new_subscription)
    await db.commit()
    await db.refresh(new_subscription)

    result = new_subscription.__dict__.copy()
    result["next_payment_date"] = next_payment

    return SubscriptionResponse(**result)


@router.get("", response_model=SubscriptionListResponse)
async def get_subscriptions(
    category: Optional[SubscriptionCategory] = Query(None, description="Filter by category"),
    is_active: Optional[bool] = Query(None, description="Filter by active status"),
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    Depends(get_current_user_id)
    """
    Get all subscriptions for the current user.
    
    Optional filters:
    - category: Filter by subscription category
    - is_active: Filter by active/inactive status
    """
    query = select(Subscription).where(Subscription.user_id == user_id)
    
    if category:
        query = query.where(Subscription.category == category)
    
    if is_active is not None:
        query = query.where(Subscription.is_active == is_active)
    
    query = query.order_by(Subscription.amount)
    
    result = await db.execute(query)
    subscriptions = result.scalars().all()

    result_subscription = []
    for sub in subscriptions:
        next_payment = calculate_next_payment_date(sub.first_payment_date, sub.payment_period)

        sub = sub.__dict__
        sub["next_payment_date"] = next_payment
        result_subscription.append(SubscriptionResponse(**sub))

    return SubscriptionListResponse(
        subscriptions=result_subscription,
        total=len(result_subscription)
    )


@router.get("/{subscription_id}", response_model=SubscriptionResponse)
async def get_subscription(
    subscription_id: int,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Get a specific subscription by ID.
    """
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.id == subscription_id,
                Subscription.user_id == user_id
            )
        )
    )
    subscription = result.scalar_one_or_none()

    if not subscription:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Subscription not found"
        )
    next_payment = calculate_next_payment_date(subscription.first_payment_date, subscription.payment_period)
    subscription = subscription.__dict__
    subscription["next_payment_date"] = next_payment

    return SubscriptionResponse(**subscription)



@router.put("/{subscription_id}", response_model=SubscriptionResponse)
async def update_subscription(
    subscription_id: int,
    update_data: SubscriptionUpdate,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Update a subscription.
    
    Only provided fields will be updated.
    """
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.id == subscription_id,
                Subscription.user_id == user_id
            )
        )
    )

    subscription = result.scalar_one_or_none()
    
    if not subscription:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Subscription not found"
        )
    
    # Update only provided fields
    update_dict = update_data.model_dump(exclude_unset=True)
    
    for field, value in update_dict.items():
        setattr(subscription, field, value)

    await db.commit()
    await db.refresh(subscription)

    next_payment = calculate_next_payment_date(
        subscription.first_payment_date,
        subscription.payment_period
    )

    subscription = subscription.__dict__
    subscription["next_payment_date"] = next_payment

    return SubscriptionResponse(**subscription)


@router.delete("/{subscription_id}", response_model=MessageResponse)
async def delete_subscription(
    subscription_id: int,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Delete a subscription.
    """
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.id == subscription_id,
                Subscription.user_id == user_id
            )
        )
    )
    subscription = result.scalar_one_or_none()
    
    if not subscription:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Subscription not found"
        )
    
    await db.delete(subscription)
    await db.commit()
    
    return MessageResponse(message="Subscription deleted successfully")


@router.post("/{subscription_id}/toggle", response_model=SubscriptionResponse)
async def toggle_subscription_active(
    subscription_id: int,
    user_id: int = Depends(get_current_user_id),
    db: AsyncSession = Depends(get_db)
):
    """
    Toggle subscription active/inactive status.
    
    Useful for pausing subscriptions without deleting them.
    """
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.id == subscription_id,
                Subscription.user_id == user_id
            )
        )
    )
    subscription = result.scalar_one_or_none()
    
    if not subscription:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Subscription not found"
        )
    
    subscription.is_active = not subscription.is_active
    await db.commit()
    await db.refresh(subscription)

    next_payment = calculate_next_payment_date(
        subscription.first_payment_date,
        subscription.payment_period
    )

    result = subscription.__dict__
    result["next_payment_date"] = next_payment

    return SubscriptionResponse(**result)
