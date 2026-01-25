import os
import sys

# Define path for folder 'backend'
sys.path.append(r"C:\Users\Aleks\Documents\Subscription-Tracker-App")
sys.path.append(r"D:\3course\Erasmus studying\Mobile App\Full version\backend>")

import uvicorn

if __name__ == "__main__":
    uvicorn.run("app.main:app", host="0.0.0.0", port=8001, reload=False)