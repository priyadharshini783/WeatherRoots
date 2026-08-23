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
        f"Explain briefly why {recommended_crop} is suitable "
        f"for the given farm conditions."
    )

    context = f"""
Recommended Crop:
{recommended_crop}

Farm Conditions:
Temperature: {temperature} °C
Humidity: {humidity} %
Historical Annual Rainfall: {rainfall} mm
Soil Type: {soil_type}
Water Availability: {water_availability}
Previous Crop: {previous_crop}
Season: {season}

Important:
- The rainfall value is a historical annual climate rainfall value.
- It is not today's rainfall.
- Give a short and practical explanation for a farmer.
- Explain how the crop matches the soil, season, temperature,
  rainfall and water availability.
- Previous crop may be mentioned only as crop-rotation context.
- Do not guarantee crop success.
- Do not invent fertilizer or pesticide dosages.
- Mention that actual performance may vary with local soil fertility,
  irrigation, weather variation and farming practices.
"""

    answer = generate_farming_answer(
        question=question,
        context=context
    )

    # Fallback in case Gemini is temporarily unavailable
    if (
        not answer
        or "currently unavailable" in answer.lower()
    ):
        return (
            f"{recommended_crop} is recommended because it matches "
            f"the supplied {soil_type} soil, {season} season, "
            f"{water_availability.lower()} water availability, "
            f"{temperature}°C temperature and the area's historical "
            f"annual rainfall of about {rainfall} mm. "
            f"Actual crop performance can still vary with local soil "
            f"fertility, irrigation and weather conditions."
        )

    return answer