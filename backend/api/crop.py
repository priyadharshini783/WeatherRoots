from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field

from services.crop_suitability import recommend_crops
from services.crop_advice import generate_crop_advice
from services.climate_service import get_tamil_nadu_annual_rainfall


router = APIRouter(
    prefix="/crop",
    tags=["Crop Recommendation"]
)


# ============================================================
# Request Model
# ============================================================

class CropRecommendationRequest(BaseModel):

    temperature: float = Field(
        ge=-20,
        le=60
    )

    humidity: float = Field(
        ge=0,
        le=100
    )

    # Current / recent rainfall from Android weather API
    rainfall: float = Field(
        ge=0
    )

    soil_type: str

    water_availability: str

    previous_crop: str

    season: str


# ============================================================
# Alternative Crop Model
# ============================================================

class AlternativeCrop(BaseModel):

    crop: str

    suitability_score: float


# ============================================================
# Response Model
# ============================================================

class CropRecommendationResponse(BaseModel):

    recommended_crop: str

    suitability_score: float

    alternatives: list[AlternativeCrop]

    explanation: str

    current_rainfall: float

    climate_rainfall: float

    rainfall_source: str


# ============================================================
# Crop Recommendation Endpoint
# ============================================================

@router.post(
    "/recommend",
    response_model=CropRecommendationResponse
)
def recommend_crop(
    request: CropRecommendationRequest
):

    try:

        # ----------------------------------------------------
        # STEP 1
        # Current rainfall received from Android / weather API
        # ----------------------------------------------------

        current_rainfall = request.rainfall


        # ----------------------------------------------------
        # STEP 2
        # Historical Tamil Nadu annual rainfall from IMD data
        # ----------------------------------------------------

        climate_rainfall = (
            get_tamil_nadu_annual_rainfall()
        )


        print("\n======================================")
        print("WeatherRoots Crop Recommendation")
        print("======================================")

        print(
            f"Temperature: "
            f"{request.temperature} °C"
        )

        print(
            f"Humidity: "
            f"{request.humidity} %"
        )

        print(
            f"Current rainfall: "
            f"{current_rainfall} mm"
        )

        print(
            f"Climate rainfall: "
            f"{climate_rainfall} mm"
        )

        print(
            f"Soil type: "
            f"{request.soil_type}"
        )

        print(
            f"Water availability: "
            f"{request.water_availability}"
        )

        print(
            f"Previous crop: "
            f"{request.previous_crop}"
        )

        print(
            f"Season: "
            f"{request.season}"
        )


        # ----------------------------------------------------
        # STEP 3
        # Crop suitability calculation
        #
        # IMPORTANT:
        # Use climate rainfall here.
        # Do NOT use today's/current rainfall.
        # ----------------------------------------------------

        results = recommend_crops(

            temperature=request.temperature,

            humidity=request.humidity,

            rainfall=climate_rainfall,

            soil_type=(
                request.soil_type.strip()
            ),

            water_availability=(
                request.water_availability.strip()
            ),

            season=(
                request.season.strip()
            ),

            top_k=3
        )


        if not results:

            raise ValueError(
                "No crop recommendations were generated."
            )


        # ----------------------------------------------------
        # STEP 4
        # Best crop
        # ----------------------------------------------------

        best_crop = results[0]

        recommended_crop = (
            best_crop["crop"]
        )

        suitability_score = (
            best_crop["score"]
        )


        # ----------------------------------------------------
        # STEP 5
        # Alternative crops
        # ----------------------------------------------------

        alternatives = [

            AlternativeCrop(

                crop=item["crop"],

                suitability_score=(
                    item["score"]
                )
            )

            for item in results[1:]
        ]


        # ----------------------------------------------------
        # STEP 6
        # Gemini explanation
        #
        # Give Gemini climate rainfall,
        # not current rainfall.
        # ----------------------------------------------------

        explanation = generate_crop_advice(

            recommended_crop=(
                recommended_crop
            ),

            temperature=(
                request.temperature
            ),

            humidity=(
                request.humidity
            ),

            rainfall=(
                climate_rainfall
            ),

            soil_type=(
                request.soil_type.strip()
            ),

            water_availability=(
                request.water_availability.strip()
            ),

            previous_crop=(
                request.previous_crop.strip()
            ),

            season=(
                request.season.strip()
            )
        )


        # ----------------------------------------------------
        # STEP 7
        # Final response
        # ----------------------------------------------------

        return CropRecommendationResponse(

            recommended_crop=(
                recommended_crop
            ),

            suitability_score=(
                suitability_score
            ),

            alternatives=(
                alternatives
            ),

            explanation=(
                explanation
            ),

            current_rainfall=(
                current_rainfall
            ),

            climate_rainfall=(
                climate_rainfall
            ),

            rainfall_source=(
                "IMD historical Tamil Nadu "
                "annual rainfall average"
            )
        )


    except Exception as error:

        print(
            "Crop Recommendation API Error:",
            error
        )

        raise HTTPException(

            status_code=500,

            detail=(
                "Unable to generate "
                "crop recommendation."
            )
        )