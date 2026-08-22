from fastapi import FastAPI

from api.voice import router as voice_router
from api.crop import router as crop_router


app = FastAPI(
    title="WeatherRoots AI Backend",
    description=(
        "Multilingual AI farming assistant with "
        "RAG, Gemini and ML crop recommendation"
    ),
    version="2.0"
)


# Voice Assistant API
app.include_router(voice_router)

# Crop Recommendation API
app.include_router(crop_router)


@app.get("/")
def home():
    return {
        "status": "success",
        "message": "WeatherRoots AI Backend is running"
    }