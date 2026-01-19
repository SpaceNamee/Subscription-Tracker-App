from pydantic import BaseModel, EmailStr
from typing import List

class EmailModel(BaseModel):
    addresses: List[EmailStr]