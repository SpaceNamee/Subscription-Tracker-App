from fastapi import APIRouter, Depends, Query
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, and_
from typing import List
from datetime import datetime, timedelta, timezone
from dateutil.relativedelta import relativedelta

from app.api.v1.endpoints.subscriptions import calculate_next_payment_date
from app.db.database import get_db
from app.models.subscription import Subscription, SubscriptionCategory, PaymentPeriod
from app.models.user import User
from app.schemas.analytics import (
    SpendingAnalytics,
    CategorySpending,
    UpcomingPayment,
    UpcomingPaymentsResponse,
)
from app.core.security import get_current_user_id

router = APIRouter(prefix="/analytics", tags=["Analytics"])


def get_period_range(period: str) -> tuple[datetime, datetime]:
    """
    Get start and end dates for the period.

    - "month": First day of last month to last day of last month
    - "year": First day of last year to last day of last year
    """
    now = datetime.now(timezone.utc)

    if period == "year":
        start_date = datetime(now.year - 1, 1, 1)  # Jan 1st of current year
        end_date = datetime(now.year - 1, 12, 31, 23, 59, 59)  # Dec 31st of current year
    else:  # month
        if now.month == 1:
            last_month = 12
            year = now.year - 1
        else:
            last_month = now.month - 1
            year = now.year

        start_date = datetime(year, last_month, 1)  # First day of current month
        # Last day of current month
        if last_month == 12:
            end_date = datetime(year + 1, 1, 1) - timedelta(seconds=1)
        else:
            end_date = datetime(year, last_month + 1, 1) - timedelta(seconds=1)

    return start_date, end_date


def add_period(date: datetime, period: PaymentPeriod) -> datetime:
    """Add one payment period to a date."""
    if period == PaymentPeriod.WEEKLY:
        return date + timedelta(weeks=1)
    elif period == PaymentPeriod.MONTHLY:
        return date + relativedelta(months=1)
    elif period == PaymentPeriod.QUARTERLY:
        return date + relativedelta(months=3)
    elif period == PaymentPeriod.YEARLY:
        return date + relativedelta(years=1)
    return date + relativedelta(months=1)


def get_payments_in_range(
        first_payment_date: datetime,
        payment_period: PaymentPeriod,
        amount: float,
        currency: str,
        subscription_id: int,
        subscription_name: str,
        category: SubscriptionCategory,
        start_date: datetime,
        end_date: datetime,
        logo_url: str = None
) -> List[dict]:
    """
    Get all payment dates for a subscription within a date range.

    Returns list of payment records.
    """
    payments = []
    current_date = first_payment_date

    # Move forward until we reach or pass start_date
    while current_date < start_date:
        current_date = add_period(current_date, payment_period)

    # Collect all payments within the range
    while current_date <= end_date:
        payments.append({
            "subscription_id": subscription_id,
            "subscription_name": subscription_name,
            "category": category.value,
            "amount": amount,
            "currency": currency,
            "payment_date": current_date.isoformat(),
            "logo_url": logo_url
        })
        current_date = add_period(current_date, payment_period)

    return payments


@router.get("/spending", response_model=SpendingAnalytics)
async def get_spending_analytics(
        period: str = Query("month", description="'month' or 'year'"),
        user_id: int = Depends(get_current_user_id),
        db: AsyncSession = Depends(get_db)
):
    """
    Get spending analytics for the current user.

    - period: "month" for current calendar month, "year" for current calendar year

    Returns:
    - Total actual spending (not average)
    - Breakdown by category
    - Percentage distribution
    """
    # Get user's currency
    user_result = await db.execute(select(User).where(User.id == user_id))
    user = user_result.scalar_one()

    # Get date range
    start_date, end_date = get_period_range(period)

    # Get active subscriptions
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.user_id == user_id,
                Subscription.is_active == True
            )
        )
    )
    subscriptions = result.scalars().all()

    # Calculate actual spending by category
    category_totals = {}
    total_amount = 0.0
    total_payments = 0

    for sub in subscriptions:
        # Get all payments for this subscription in the period
        payments = get_payments_in_range(
            first_payment_date=sub.first_payment_date,
            payment_period=sub.payment_period,
            amount=sub.amount,
            currency=sub.currency,
            subscription_id=sub.id,
            subscription_name=sub.name,
            category=sub.category,
            start_date=start_date,
            end_date=end_date
        )

        payment_count = len(payments)
        subscription_total = sub.amount * payment_count

        total_amount += subscription_total
        total_payments += payment_count

        if sub.category not in category_totals:
            category_totals[sub.category] = {
                "amount": 0.0,
                "count": 0,
                "payment_count": 0
            }

        category_totals[sub.category]["amount"] += subscription_total
        category_totals[sub.category]["count"] += 1
        category_totals[sub.category]["payment_count"] += payment_count

    # Build category breakdown
    by_category = []
    for category, data in category_totals.items():
        percentage = (data["amount"] / total_amount * 100) if total_amount > 0 else 0

        by_category.append(CategorySpending(
            category=category,
            total_amount=round(data["amount"], 2),
            currency=user.currency,
            subscription_count=data["count"],
            percentage=round(percentage, 1)
        ))

    # Sort by amount descending
    by_category.sort(key=lambda x: x.total_amount, reverse=True)

    return SpendingAnalytics(
        period=period,
        start_date=start_date,
        end_date=end_date,
        total_amount=round(total_amount, 2),
        currency=user.currency,
        by_category=by_category,
        subscription_count=len(subscriptions)
    )


@router.get("/spending/category/{category}")
async def get_payments_by_category(
        category: SubscriptionCategory,
        period: str = Query("month", description="'month' or 'year'"),
        user_id: int = Depends(get_current_user_id),
        db: AsyncSession = Depends(get_db)
):
    """
    Get payment history for a specific category.

    Returns all actual payments that occurred in the period for subscriptions in this category.
    """
    # Get date range
    start_date, end_date = get_period_range(period)

    # Get subscriptions in this category
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.user_id == user_id,
                Subscription.category == category,
                Subscription.is_active == True
            )
        )
    )
    subscriptions = result.scalars().all()

    # Collect all payments
    all_payments = []
    total_amount = 0.0

    for sub in subscriptions:
        payments = get_payments_in_range(
            first_payment_date=sub.first_payment_date,
            payment_period=sub.payment_period,
            amount=sub.amount,
            currency=sub.currency,
            subscription_id=sub.id,
            subscription_name=sub.name,
            category=sub.category,
            start_date=start_date,
            end_date=end_date,
            logo_url=sub.logo_url
        )
        all_payments.extend(payments)
        total_amount += sum(p["amount"] for p in payments)

    # Sort by payment date
    all_payments.sort(key=lambda x: x["payment_date"])

    return {
        "category": category.value,
        "period": period,
        "start_date": start_date.isoformat(),
        "end_date": end_date.isoformat(),
        "total_amount": round(total_amount, 2),
        "payment_count": len(all_payments),
        "payments": all_payments
    }


@router.get("/history")
async def get_payment_history(
        period: str = Query("month", description="'month' or 'year'"),
        user_id: int = Depends(get_current_user_id),
        db: AsyncSession = Depends(get_db)
):
    """
    Get full payment history for all subscriptions.

    Returns all actual payments that occurred in the period (no category filter).
    """
    # Get user's currency
    user_result = await db.execute(select(User).where(User.id == user_id))
    user = user_result.scalar_one()

    # Get date range
    start_date, end_date = get_period_range(period)

    # Get all active subscriptions
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.user_id == user_id,
                Subscription.is_active == True
            )
        )
    )
    subscriptions = result.scalars().all()

    # Collect all payments
    all_payments = []
    total_amount = 0.0

    for sub in subscriptions:
        payments = get_payments_in_range(
            first_payment_date=sub.first_payment_date,
            payment_period=sub.payment_period,
            amount=sub.amount,
            currency=sub.currency,
            subscription_id=sub.id,
            subscription_name=sub.name,
            category=sub.category,
            start_date=start_date,
            end_date=end_date,
            logo_url=sub.logo_url
        )
        all_payments.extend(payments)
        total_amount += sum(p["amount"] for p in payments)

    # Sort by payment date
    all_payments.sort(key=lambda x: x["payment_date"])

    return {
        "period": period,
        "start_date": start_date.isoformat(),
        "end_date": end_date.isoformat(),
        "total_amount": round(total_amount, 2),
        "payment_count": len(all_payments),
        "currency": user.currency,
        "payments": all_payments
    }


@router.get("/upcoming", response_model=UpcomingPaymentsResponse)
async def get_upcoming_payments(
        days: int = Query(30, description="Number of days to look ahead"),
        user_id: int = Depends(get_current_user_id),
        db: AsyncSession = Depends(get_db)
):
    """
    Get upcoming payments for the next N days.

    - days: How many days ahead to check (default: 30)

    Useful for notifications and payment reminders.
    """
    # Get user's currency
    user_result = await db.execute(select(User).where(User.id == user_id))
    user = user_result.scalar_one()

    now = datetime.now(timezone.utc)
    end_date = now + timedelta(days=days)

    # Get all active subscriptions
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.user_id == user_id,
                Subscription.is_active == True
            )
        )
    )
    subscriptions = result.scalars().all()

    payments = []
    total_amount = 0.0

    for sub in subscriptions:
        next_payment = calculate_next_payment_date(sub.first_payment_date, sub.payment_period)

        # Only include if next payment is within the requested range
        if now <= next_payment <= end_date:
            days_until = (next_payment - now).days
            payments.append(UpcomingPayment(
                subscription_id=sub.id,
                subscription_name=sub.name,
                amount=sub.amount,
                currency=sub.currency,
                payment_date=next_payment,
                days_until_payment=days_until
            ))
            total_amount += sub.amount

    # Sort by payment date
    payments.sort(key=lambda x: x.payment_date)

    return UpcomingPaymentsResponse(
        payments=payments,
        total_upcoming_amount=round(total_amount, 2),
        currency=user.currency
    )


@router.get("/summary")
async def get_quick_summary(
        user_id: int = Depends(get_current_user_id),
        db: AsyncSession = Depends(get_db)
):
    """
    Get a quick summary for the dashboard.

    Returns:
    - Active subscription count
    - Current month spending (actual)
    - Current year spending (actual)
    - Next payment info
    """

    # Get user's currency
    user_result = await db.execute(select(User).where(User.id == user_id))
    user = user_result.scalar_one()

    # Get active subscriptions
    result = await db.execute(
        select(Subscription).where(
            and_(
                Subscription.user_id == user_id,
                Subscription.is_active == True
            )
        )
    )
    subscriptions = result.scalars().all()

    # Calculate actual spending for current month
    month_start, month_end = get_period_range("month")
    monthly_total = 0.0

    for sub in subscriptions:
        payments = get_payments_in_range(
            first_payment_date=sub.first_payment_date,
            payment_period=sub.payment_period,
            amount=sub.amount,
            currency=sub.currency,
            subscription_id=sub.id,
            subscription_name=sub.name,
            category=sub.category,
            start_date=month_start,
            end_date=month_end
        )
        monthly_total += sub.amount * len(payments)

    # Calculate actual spending for current year
    year_start, year_end = get_period_range("year")
    yearly_total = 0.0

    for sub in subscriptions:
        payments = get_payments_in_range(
            first_payment_date=sub.first_payment_date,
            payment_period=sub.payment_period,
            amount=sub.amount,
            currency=sub.currency,
            subscription_id=sub.id,
            subscription_name=sub.name,
            category=sub.category,
            start_date=year_start,
            end_date=year_end
        )
        yearly_total += sub.amount * len(payments)

    # Find next upcoming payment
    now = datetime.now(timezone.utc)
    next_payment = None
    soonest_date = None

    for sub in subscriptions:
        sub_next_payment = calculate_next_payment_date(sub.first_payment_date, sub.payment_period)
        if soonest_date is None or sub_next_payment < soonest_date:
            soonest_date = sub_next_payment
            next_payment = {
                "subscription_name": sub.name,
                "amount": sub.amount,
                "currency": sub.currency,
                "date": sub_next_payment.isoformat(),
                "days_until": (sub_next_payment - now).days
            }

    return {
        "active_subscriptions": len(subscriptions),
        "monthly_total": round(monthly_total, 2),
        "yearly_total": round(yearly_total, 2),
        "currency": user.currency,
        "current_month": {
            "start": month_start.isoformat(),
            "end": month_end.isoformat()
        },
        "current_year": {
            "start": year_start.isoformat(),
            "end": year_end.isoformat()
        },
        "next_payment": next_payment
    }