from datetime import datetime, timezone

from sqlalchemy import Column, Integer, String, Float, Boolean, DateTime, ForeignKey, Enum
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
import enum

from app.db.database import Base


class PaymentPeriod(str, enum.Enum):
    WEEKLY = "weekly"
    MONTHLY = "monthly"
    QUARTERLY = "quarterly"
    YEARLY = "yearly"


class SubscriptionCategory(str, enum.Enum):
    STREAMING = "streaming"
    MUSIC = "music"
    GAMING = "gaming"
    PRODUCTIVITY = "productivity"
    CLOUD_STORAGE = "cloud_storage"
    NEWS = "news"
    FITNESS = "fitness"
    EDUCATION = "education"
    FOOD_DELIVERY = "food_delivery"
    SHOPPING = "shopping"
    FINANCE = "finance"
    VPN = "vpn"
    OTHER = "other"


class Subscription(Base):
    __tablename__ = "subscriptions"
    __table_args__ = {'extend_existing': True}
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False, index=True)
    
    # Subscription details
    name = Column(String(255), nullable=False)
    description = Column(String(500), nullable=True)
    category = Column(Enum(SubscriptionCategory), default=SubscriptionCategory.OTHER)
    
    # Payment info
    amount = Column(Float, nullable=False)
    currency = Column(String(3), default="USD")
    payment_period = Column(Enum(PaymentPeriod), nullable=False)
    first_payment_date = Column(DateTime(timezone=True), nullable=False)

    # Optional metadata
    logo_url = Column(String(500), nullable=True)
    website_url = Column(String(500), nullable=True)  # For "Manage" button
    is_active = Column(Boolean, default=True)
    
    # Timestamps
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    updated_at = Column(DateTime(timezone=True), onupdate=func.now())
    
    # Relationships
    user = relationship("User", back_populates="subscriptions")
    
    def __repr__(self):
        return f"<Subscription {self.name} - {self.amount} {self.currency}/{self.payment_period}>"
