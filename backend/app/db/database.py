from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession, async_sessionmaker
from sqlalchemy.orm import DeclarativeBase

from app.core.config import settings
from pathlib import Path



print("Database url : ", settings.DATABASE_URL)
# Create async engine
engine = create_async_engine(
    f"sqlite+aiosqlite:///{Path(__file__).resolve().parent.parent.parent}/subscriptions.db",
    echo=settings.DEBUG,  # Log SQL queries in debug mode
)

# Session factory
async_session_maker = async_sessionmaker(
    engine,
    class_=AsyncSession,
    expire_on_commit=False,
)


# Base class for all models
class Base(DeclarativeBase):
    pass


async def get_db() -> AsyncSession:
    """
    Dependency that provides a database session.
    Use in endpoints: db: AsyncSession = Depends(get_db)
    """
    async with async_session_maker() as session:
        try:
            yield session
            await session.commit()
        except Exception:
            await session.rollback()
            raise
        finally:
            await session.close()


async def init_db():
    """Create all tables. Call this on startup."""
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
