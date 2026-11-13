"""
Bunon AI service - Design similarity check (F2, Sprint 2).
Sprint 1: only a health check so everyone can verify their Python setup.

Run:  pip install -r requirements.txt
      uvicorn main:app --reload --port 8000
"""
from fastapi import FastAPI

app = FastAPI(title="Bunon Design Similarity Service")


@app.get("/health")
def health():
    return {"status": "ok"}

# TODO (Sprint 2, F2): POST /similarity  (image upload) -> {"score": 0.0-1.0, "matches": [...]}
