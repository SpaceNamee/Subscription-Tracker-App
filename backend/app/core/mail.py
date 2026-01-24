from dns.message import Message
from fastapi_mail import FastMail, ConnectionConfig, MessageSchema, MessageType
from app.core.config import settings
from pathlib import Path
from pydantic import EmailStr
from typing import List

BASE_DIR = Path(__file__).resolve().parent.parent

mail_config = ConnectionConfig(
    MAIL_USERNAME= settings.MAIL_USERNAME,
    MAIL_PASSWORD = settings.MAIL_PASSWORD,
    MAIL_FROM = settings.MAIL_FROM,
    MAIL_PORT = 587,
    MAIL_SERVER = settings.MAIL_SERVER,
    MAIL_FROM_NAME= settings.MAIL_FROM_NAME,
    MAIL_STARTTLS = True,
    MAIL_SSL_TLS = False,
    USE_CREDENTIALS = True,
    VALIDATE_CERTS = True,
    TEMPLATE_FOLDER=Path(BASE_DIR, 'templates')
)

mail = FastMail(
    config = mail_config
)

def create_message(recipients: List[EmailStr], subject: str, body: str | list | None = None):
    message = MessageSchema(
        recipients=recipients,
        subject=subject,
        body=body,
        subtype=MessageType.html
    )

    return message

