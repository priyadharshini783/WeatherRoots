from services.llm_service import generate_farming_answer


def generate_crop_advice(
    recommended_crop: str,
    temperature: float,
    humidity: float,
    rainfall: float,
    soil_type: str,
    water_availability: str,
    previous_crop: str,
    season: str
) -> str:

    question = (
        f"Explain why {recommended_crop} may be suitable "
        f"for the provided farming conditions."
    )

    context = f"""
Crop recommendation result:
{recommended_crop}

Farm and weather conditions:

Temperature: {temperature} °C
Humidity: {humidity} %
Rainfall: {rainfall} mm
Soil type: {soil_type}
Water availability: {water_availability}
Previous crop: {previous_crop}
Season: {season}

Give a short, simple explanation for a farmer.

Do not invent exact fertilizer or pesticide dosages.
Do not claim the crop is guaranteed to succeed.
Mention that actual results can depend on local soil,
weather, irrigation, and farming practices.
"""

    explanation = generate_farming_answer(
        question=question,
        context=context
    )

    return explanation