from pydantic import BaseModel
from typing import List, Dict
from datetime import datetime
from backend.app.models.subscription import SubscriptionCategory


class CategorySpending(BaseModel):
    """Spending breakdown for a single category."""
    category: SubscriptionCategory
    total_amount: float
    currency: str
    subscription_count: int
    percentage: float  # Percentage of total spending


class SpendingAnalytics(BaseModel):
    """Overall spending analytics."""
    period: str  # "month" or "year"
    start_date: datetime
    end_date: datetime
    total_amount: float
    currency: str
    by_category: List[CategorySpending]
    subscription_count: int


class UpcomingPayment(BaseModel):
    """Upcoming payment notification data."""
    subscription_id: int
    subscription_name: str
    amount: float
    currency: str
    payment_date: datetime
    days_until_payment: int


class UpcomingPaymentsResponse(BaseModel):
    """List of upcoming payments."""
    payments: List[UpcomingPayment]
    total_upcoming_amount: float
    currency: str
