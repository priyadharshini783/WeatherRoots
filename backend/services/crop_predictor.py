from pathlib import Path

import joblib
import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_PATH = BASE_DIR / "ml" / "crop_model.joblib"


if not MODEL_PATH.exists():
    raise FileNotFoundError(
        "Crop model not found. Run ml/train_crop_model.py first."
    )


crop_model = joblib.load(MODEL_PATH)


def predict_crop(
    temperature: float,
    humidity: float,
    rainfall: float,
    soil_type: str,
    water_availability: str,
    previous_crop: str,
    season: str
) -> dict:

    input_data = pd.DataFrame(
        [
            {
                "temperature": temperature,
                "humidity": humidity,
                "rainfall": rainfall,
                "soil_type": soil_type,
                "water_availability": water_availability,
                "previous_crop": previous_crop,
                "season": season
            }
        ]
    )

    prediction = crop_model.predict(input_data)[0]

    confidence = None

    if hasattr(crop_model, "predict_proba"):
        probabilities = crop_model.predict_proba(input_data)[0]
        confidence = float(max(probabilities))

    return {
        "recommended_crop": str(prediction),
        "confidence": confidence
    }