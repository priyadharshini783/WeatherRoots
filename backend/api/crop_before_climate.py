from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field

from services.crop_suitability import recommend_crops
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


class AlternativeCrop(BaseModel):

    crop: str
    suitability_score: float


class CropRecommendationResponse(BaseModel):

    recommended_crop: str

    suitability_score: float

    alternatives: list[AlternativeCrop]

    explanation: str


@router.post(
    "/recommend",
    response_model=CropRecommendationResponse
)
def recommend_crop(
    request: CropRecommendationRequest
):

    try:

        # -------------------------------------------------
        # STEP 1
        # Calculate crop suitability
        # -------------------------------------------------

        results = recommend_crops(

            temperature=request.temperature,

            humidity=request.humidity,

            rainfall=request.rainfall,

            soil_type=request.soil_type.strip(),

            water_availability=(
                request.water_availability.strip()
            ),

            season=request.season.strip(),

            top_k=3
        )


        if not results:

            raise ValueError(
                "No crop recommendations were generated."
            )


        # -------------------------------------------------
        # STEP 2
        # Best crop
        # -------------------------------------------------

        best_crop = results[0]

        recommended_crop = best_crop["crop"]

        suitability_score = best_crop["score"]


        # -------------------------------------------------
        # STEP 3
        # Alternative crops
        # -------------------------------------------------

        alternatives = [

            AlternativeCrop(
                crop=item["crop"],
                suitability_score=item["score"]
            )

            for item in results[1:]
        ]


        # -------------------------------------------------
        # STEP 4
        # Gemini explanation
        # -------------------------------------------------

        explanation = generate_crop_advice(

            recommended_crop=recommended_crop,

            temperature=request.temperature,

            humidity=request.humidity,

            rainfall=request.rainfall,

            soil_type=request.soil_type.strip(),

            water_availability=(
                request.water_availability.strip()
            ),

            previous_crop=(
                request.previous_crop.strip()
            ),

            season=request.season.strip()
        )


        # -------------------------------------------------
        # STEP 5
        # Response
        # -------------------------------------------------

        return CropRecommendationResponse(

            recommended_crop=recommended_crop,

            suitability_score=suitability_score,

            alternatives=alternatives,

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
                "Unable to generate "
                "crop recommendation."
            )
        )