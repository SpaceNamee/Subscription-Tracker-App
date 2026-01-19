from app.schemas.user import (
    UserCreate,
    UserLogin,
    UserUpdate,
    PasswordChange,
    EmailChange,
    UserResponse,
    TokenResponse,
    MessageResponse,
)
from app.schemas.subscription import (
    SubscriptionCreate,
    SubscriptionUpdate,
    SubscriptionResponse,
    SubscriptionListResponse,
    PopularSubscription,
    POPULAR_SUBSCRIPTIONS,
)
from app.schemas.analytics import (
    CategorySpending,
    SpendingAnalytics,
    UpcomingPayment,
    UpcomingPaymentsResponse,
)

__all__ = [
    # User schemas
    "UserCreate",
    "UserLogin",
    "UserUpdate",
    "PasswordChange",
    "EmailChange",
    "UserResponse",
    "TokenResponse",
    "MessageResponse",
    # Subscription schemas
    "SubscriptionCreate",
    "SubscriptionUpdate",
    "SubscriptionResponse",
    "SubscriptionListResponse",
    "PopularSubscription",
    "POPULAR_SUBSCRIPTIONS",
    # Analytics schemas
    "CategorySpending",
    "SpendingAnalytics",
    "UpcomingPayment",
    "UpcomingPaymentsResponse",
]
