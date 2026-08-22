from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field

from services.crop_predictor import predict_crop
from services.crop_advice import generate_crop_advice


router = APIRouter(
    prefix="/crop",
    tags=["Crop Recommendation"]
)


class CropRecommendationRequest(BaseModel):

    temperature: float = Field(
        ge=-20,
        le=60
    )

    humidity: float = Field(
        ge=0,
        le=100
    )

    rainfall: float = Field(
        ge=0
    )

    soil_type: str
    water_availability: str
    previous_crop: str
    season: str


class CropRecommendationResponse(BaseModel):

    recommended_crop: str

    confidence: float | None

    explanation: str


@router.post(
    "/recommend",
    response_model=CropRecommendationResponse
)
def recommend_crop(
    request: CropRecommendationRequest
):

    try:

        # ---------------------------------------
        # Step 1: Random Forest prediction
        # ---------------------------------------

        result = predict_crop(
            temperature=request.temperature,
            humidity=request.humidity,
            rainfall=request.rainfall,
            soil_type=request.soil_type.strip(),
            water_availability=request.water_availability.strip(),
            previous_crop=request.previous_crop.strip(),
            season=request.season.strip()
        )


        recommended_crop = result[
            "recommended_crop"
        ]

        confidence = result[
            "confidence"
        ]


        print("\n==============================")
        print("Crop Recommendation")
        print("==============================")

        print("Recommended Crop:")
        print(recommended_crop)

        print("Confidence:")
        print(confidence)


        # ---------------------------------------
        # Step 2: Gemini explanation
        # ---------------------------------------

        explanation = generate_crop_advice(
            recommended_crop=recommended_crop,
            temperature=request.temperature,
            humidity=request.humidity,
            rainfall=request.rainfall,
            soil_type=request.soil_type.strip(),
            water_availability=request.water_availability.strip(),
            previous_crop=request.previous_crop.strip(),
            season=request.season.strip()
        )


        print("\nGemini Explanation:")
        print(explanation)


        # ---------------------------------------
        # Step 3: API response
        # ---------------------------------------

        return CropRecommendationResponse(

            recommended_crop=recommended_crop,

            confidence=confidence,

            explanation=explanation
        )


    except Exception as error:

        print(
            "Crop Recommendation API Error:",
            error
        )

        raise HTTPException(
            status_code=500,
            detail=(
                "Unable to generate crop recommendation."
            )
        )