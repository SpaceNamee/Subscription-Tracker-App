import os
import sys

# Define o caminho base para o Python encontrar a pasta 'backend'
sys.path.append(r"C:\Users\Aleks\Documents\Subscription-Tracker-App")

import uvicorn

if __name__ == "__main__":
    # O segredo aqui é o reload=False para evitar o erro de tabelas duplicadas
    uvicorn.run("app.main:app", host="0.0.0.0", port=8001, reload=False)