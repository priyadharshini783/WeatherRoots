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
Recommended Crop:
{recommended_crop}

Farm Conditions:

Temperature:
{temperature} °C

Humidity:
{humidity} %

Historical Annual Rainfall:
{rainfall} mm

Soil Type:
{soil_type}

Water Availability:
{water_availability}

Previous Crop:
{previous_crop}

Season:
{season}

Important Information:

- The rainfall value above is a historical annual climate rainfall value.
- It is NOT today's rainfall.
- Current rainfall may be different.
- Explain the recommendation using the provided farm and climate conditions.
- Keep the explanation short and simple for farmers.
- Do not guarantee crop success.
- Do not invent exact fertilizer or pesticide dosages.
- Mention that actual results can depend on local soil fertility,
  irrigation, weather variation and farming practices.
"""

    return generate_farming_answer(
        question=question,
        context=context
    )