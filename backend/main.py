from fastapi import FastAPI
from api.voice import router


app = FastAPI(
    title="WeatherRoots AI Voice Assistant Backend",
    description="Multilingual AI farming assistant backend",
    version="1.0"
)


app.include_router(router)


@app.get("/")
def home():

    return {
        "status": "success",
        "message": "WeatherRoots AI Backend is running"
    }