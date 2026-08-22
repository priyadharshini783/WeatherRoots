from pathlib import Path

import joblib
import pandas as pd

from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder


BASE_DIR = Path(__file__).resolve().parent.parent

DATA_PATH = BASE_DIR / "data" / "crop_recommendation.csv"
MODEL_PATH = BASE_DIR / "ml" / "crop_model.joblib"


def train_model():

    print("Loading crop recommendation dataset...")

    data = pd.read_csv(DATA_PATH)

    print(f"Rows loaded: {len(data)}")

    feature_columns = [
        "temperature",
        "humidity",
        "rainfall",
        "soil_type",
        "water_availability",
        "previous_crop",
        "season",
    ]

    target_column = "recommended_crop"

    X = data[feature_columns]
    y = data[target_column]

    numeric_features = [
        "temperature",
        "humidity",
        "rainfall",
    ]

    categorical_features = [
        "soil_type",
        "water_availability",
        "previous_crop",
        "season",
    ]

    preprocessor = ColumnTransformer(
        transformers=[
            (
                "categorical",
                OneHotEncoder(handle_unknown="ignore"),
                categorical_features,
            ),
            (
                "numeric",
                "passthrough",
                numeric_features,
            ),
        ]
    )

    classifier = RandomForestClassifier(
        n_estimators=300,
        random_state=42,
        class_weight="balanced"
    )

    pipeline = Pipeline(
        steps=[
            ("preprocessor", preprocessor),
            ("classifier", classifier),
        ]
    )

    X_train, X_test, y_train, y_test = train_test_split(
        X,
        y,
        test_size=0.25,
        random_state=42
    )

    print("Training model...")

    pipeline.fit(
        X_train,
        y_train
    )

    predictions = pipeline.predict(
        X_test
    )

    accuracy = accuracy_score(
        y_test,
        predictions
    )

    print("\nAccuracy:")
    print(f"{accuracy:.2%}")

    print("\nClassification Report:")
    print(
        classification_report(
            y_test,
            predictions,
            zero_division=0
        )
    )

    joblib.dump(
        pipeline,
        MODEL_PATH
    )

    print("\nModel saved successfully:")
    print(MODEL_PATH)


if __name__ == "__main__":
    train_model()