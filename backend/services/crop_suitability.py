from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent

DATA_FILE = (
    BASE_DIR
    / "data"
    / "crop_suitability.csv"
)


crop_data = pd.read_csv(DATA_FILE)


def normalize(value: str) -> str:
    return str(value).strip().lower()


def numeric_score(
    value: float,
    minimum: float,
    maximum: float
) -> float:

    # Disabled / unavailable range
    if minimum == 0 and maximum == 0:
        return 0.0

    # Invalid range
    if maximum <= minimum:
        return 0.0

    midpoint = (
        minimum + maximum
    ) / 2

    half_range = (
        maximum - minimum
    ) / 2

    # Inside preferred range
    if minimum <= value <= maximum:

        distance = abs(
            value - midpoint
        )

        score = (
            1 - (
                distance /
                half_range
            ) * 0.30
        )

        return max(
            0.70,
            min(1.0, score)
        )

    # Slightly outside preferred range
    margin = (
        maximum - minimum
    ) * 0.25

    if minimum - margin <= value < minimum:

        distance = (
            minimum - value
        )

        score = (
            0.70 *
            (
                1 -
                distance / margin
            )
        )

        return max(
            0.0,
            score
        )

    if maximum < value <= maximum + margin:

        distance = (
            value - maximum
        )

        score = (
            0.70 *
            (
                1 -
                distance / margin
            )
        )

        return max(
            0.0,
            score
        )

    return 0.0

def category_score(
    value: str,
    allowed_values: str
) -> float:

    user_value = normalize(value)

    allowed = [
        normalize(item)
        for item in str(
            allowed_values
        ).split("|")
    ]

    if user_value in allowed:
        return 1.0

    return 0.0


def water_score(
    user_water: str,
    crop_requirement: str
) -> float:

    levels = {
        "low": 1,
        "medium": 2,
        "high": 3
    }

    user_level = levels.get(
        normalize(user_water)
    )

    required_level = levels.get(
        normalize(crop_requirement)
    )

    if (
        user_level is None
        or required_level is None
    ):
        return 0.0

    if user_level >= required_level:
        return 1.0

    if required_level - user_level == 1:
        return 0.4

    return 0.0


def recommend_crops(
    temperature: float,
    humidity: float,
    rainfall: float,
    soil_type: str,
    water_availability: str,
    season: str,
    top_k: int = 3
):

    results = []

    for _, row in crop_data.iterrows():

        temp = numeric_score(
            temperature,
            float(row["min_temperature"]),
            float(row["max_temperature"])
        )

        humidity_score = numeric_score(
            humidity,
            float(row["min_humidity"]),
            float(row["max_humidity"])
        )

        rain = numeric_score(
            rainfall,
            float(row["min_rainfall"]),
            float(row["max_rainfall"])
        )

        soil = category_score(
            soil_type,
            row["soil_types"]
        )

        season_score = category_score(
            season,
            row["seasons"]
        )

        water = water_score(
            water_availability,
            row["water_requirement"]
        )

        # Until numerical ranges are validated,
        # soil, season and water carry more weight.
        total_score = (
    temp * 0.25
    + rain * 0.20
    + soil * 0.25
    + season_score * 0.20
    + water * 0.10
)

        results.append(
            {
                "crop": row["crop"],
                "score": round(
                    total_score * 100,
                    2
                ),
                "details": {
                    "temperature_score": temp,
                    "humidity_score":
                        humidity_score,
                    "rainfall_score": rain,
                    "soil_score": soil,
                    "season_score":
                        season_score,
                    "water_score": water
                }
            }
        )

    results.sort(
        key=lambda x: x["score"],
        reverse=True
    )

    return results[:top_k]