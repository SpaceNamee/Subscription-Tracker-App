from pydantic import BaseModel, Field, field_validator
from typing import Optional, List
from datetime import datetime, timezone
from backend.app.models.subscription import PaymentPeriod, SubscriptionCategory


# ============== Request Schemas ==============

class SubscriptionCreate(BaseModel):
    """Schema for creating a new subscription."""
    name: str = Field(..., min_length=1, max_length=255)
    description: Optional[str] = Field(None, max_length=500)
    category: SubscriptionCategory = SubscriptionCategory.OTHER
    amount: float = Field(..., gt=0)
    currency: str = Field(default="USD", min_length=3, max_length=3)
    payment_period: PaymentPeriod
    first_payment_date: Optional[datetime] = None # first only mean in application context

    website_url: Optional[str] = None
    logo_url: Optional[str] = None

    @field_validator('first_payment_date')
    @classmethod
    def validate_first_payment_date(cls, v: Optional[datetime]) -> Optional[datetime]:
        """Ensure next_payment_date is not in the past."""
        if v is None:
            return v
        now = datetime.now(v.tzinfo) if v.tzinfo else datetime.now(timezone.utc)
        if v > now:
            raise ValueError('Last payment date cannot be in the future.')
        return v


class SubscriptionUpdate(BaseModel):
    """Schema for updating a subscription."""
    name: Optional[str] = Field(None, min_length=1, max_length=255)
    description: Optional[str] = Field(None, max_length=500)
    category: Optional[SubscriptionCategory] = None
    amount: Optional[float] = Field(None, gt=0)
    currency: Optional[str] = Field(None, min_length=3, max_length=3)
    payment_period: Optional[PaymentPeriod] = None
    # first_payment_date: Optional[datetime] = None # in application context
    # next_payment_date: Optional[datetime] = None
    website_url: Optional[str] = None
    logo_url: Optional[str] = None
    is_active: Optional[bool] = None


# ============== Response Schemas ==============

class SubscriptionResponse(BaseModel):
    """Schema for subscription in responses."""
    id: int
    name: str
    description: Optional[str]
    category: SubscriptionCategory
    amount: float
    currency: str
    payment_period: PaymentPeriod
    first_payment_date: datetime
    next_payment_date: datetime
    website_url: Optional[str]
    logo_url: Optional[str]
    is_active: bool
    created_at: datetime
    
    class Config:
        from_attributes = True


class SubscriptionListResponse(BaseModel):
    """Schema for list of subscriptions."""
    subscriptions: List[SubscriptionResponse]
    total: int


# ============== Popular Subscription Templates ==============

class PopularSubscription(BaseModel):
    """Pre-defined popular subscription for quick adding."""
    name: str
    category: SubscriptionCategory
    logo_url: Optional[str]
    website_url: Optional[str]
    suggested_amount: Optional[float]  # Common price
    suggested_period: PaymentPeriod


# Pre-defined popular subscriptions
POPULAR_SUBSCRIPTIONS: List[PopularSubscription] = [
    PopularSubscription(
        name="Netflix",
        category=SubscriptionCategory.STREAMING,
        logo_url="https://www.google.com/s2/favicons?domain=netflix.com&sz=128",
        website_url="https://www.netflix.com/account",
        suggested_amount=15.49,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Spotify",
        category=SubscriptionCategory.MUSIC,
        logo_url="https://www.google.com/s2/favicons?domain=spotify.com&sz=128",
        website_url="https://www.spotify.com/account",
        suggested_amount=10.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="YouTube Premium",
        category=SubscriptionCategory.STREAMING,
        logo_url="https://www.google.com/s2/favicons?domain=youtube.com&sz=128",
        website_url="https://www.youtube.com/paid_memberships",
        suggested_amount=13.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Disney+",
        category=SubscriptionCategory.STREAMING,
        logo_url="https://www.google.com/s2/favicons?domain=disneyplus.com&sz=128",
        website_url="https://www.disneyplus.com/account",
        suggested_amount=13.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Apple Music",
        category=SubscriptionCategory.MUSIC,
        logo_url="https://www.google.com/s2/favicons?domain=apple.com&sz=128",
        website_url="https://music.apple.com/account",
        suggested_amount=10.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Amazon Prime",
        category=SubscriptionCategory.SHOPPING,
        logo_url="https://www.google.com/s2/favicons?domain=amazon.com&sz=128",
        website_url="https://www.amazon.com/mc",
        suggested_amount=14.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Xbox Game Pass",
        category=SubscriptionCategory.GAMING,
        logo_url="https://www.google.com/s2/favicons?domain=xbox.com&sz=128",
        website_url="https://account.xbox.com/subscriptions",
        suggested_amount=16.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="PlayStation Plus",
        category=SubscriptionCategory.GAMING,
        logo_url="https://www.google.com/s2/favicons?domain=playstation.com&sz=128",
        website_url="https://store.playstation.com/subscriptions",
        suggested_amount=17.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="iCloud+",
        category=SubscriptionCategory.CLOUD_STORAGE,
        logo_url="https://www.google.com/s2/favicons?domain=icloud.com&sz=128",
        website_url="https://www.icloud.com/settings/",
        suggested_amount=2.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Google One",
        category=SubscriptionCategory.CLOUD_STORAGE,
        logo_url="https://www.google.com/s2/favicons?domain=one.google.com&sz=128",
        website_url="https://one.google.com/",
        suggested_amount=2.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="Dropbox",
        category=SubscriptionCategory.CLOUD_STORAGE,
        logo_url="https://www.google.com/s2/favicons?domain=dropbox.com&sz=128",
        website_url="https://www.dropbox.com/account",
        suggested_amount=11.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
    PopularSubscription(
        name="NordVPN",
        category=SubscriptionCategory.VPN,
        logo_url="https://www.google.com/s2/favicons?domain=nordvpn.com&sz=128",
        website_url="https://my.nordaccount.com/",
        suggested_amount=12.99,
        suggested_period=PaymentPeriod.MONTHLY
    ),
]
