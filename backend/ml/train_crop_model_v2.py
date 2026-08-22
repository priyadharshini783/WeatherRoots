from pathlib import Path

import joblib
import pandas as pd

from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder


# ============================================================
# Paths
# ============================================================

BASE_DIR = Path(__file__).resolve().parent.parent

DATA_FILE = (
    BASE_DIR
    / "data"
    / "processed"
    / "weatherroots_crop_dataset.csv"
)

MODEL_FILE = (
    BASE_DIR
    / "ml"
    / "crop_model_v2.joblib"
)


# ============================================================
# Load dataset
# ============================================================

print("Loading WeatherRoots processed dataset...")

df = pd.read_csv(DATA_FILE)

print(f"Rows loaded: {len(df)}")


# ============================================================
# Clean dataset
# ============================================================

required_columns = [
    "district",
    "season",
    "rainfall",
    "soil_moisture",
    "water_availability",
    "recommended_crop"
]

df = df.dropna(
    subset=[
        "district",
        "season",
        "rainfall",
        "recommended_crop"
    ]
)

# Keep Unknown as a valid category for now.
df["water_availability"] = (
    df["water_availability"]
    .fillna("Unknown")
)

# Fill missing numeric soil moisture with the median.
df["soil_moisture"] = (
    df["soil_moisture"]
    .fillna(
        df["soil_moisture"].median()
    )
)


# ============================================================
# Features and target
# ============================================================

features = [
    "district",
    "season",
    "rainfall",
    "soil_moisture",
    "water_availability"
]

X = df[features]

y = df["recommended_crop"]


# ============================================================
# Column types
# ============================================================

categorical_features = [
    "district",
    "season",
    "water_availability"
]

numeric_features = [
    "rainfall",
    "soil_moisture"
]


# ============================================================
# Preprocessing
# ============================================================

preprocessor = ColumnTransformer(
    transformers=[
        (
            "categorical",
            OneHotEncoder(
                handle_unknown="ignore"
            ),
            categorical_features
        ),
        (
            "numeric",
            "passthrough",
            numeric_features
        )
    ]
)


# ============================================================
# Random Forest
# ============================================================

classifier = RandomForestClassifier(
    n_estimators=300,
    random_state=42,
    class_weight="balanced",
    n_jobs=-1
)


model = Pipeline(
    steps=[
        (
            "preprocessor",
            preprocessor
        ),
        (
            "classifier",
            classifier
        )
    ]
)


# ============================================================
# Train/Test Split
# ============================================================

X_train, X_test, y_train, y_test = (
    train_test_split(
        X,
        y,
        test_size=0.20,
        random_state=42,
        stratify=y
    )
)


print("\nTraining WeatherRoots Crop Model V2...")

model.fit(
    X_train,
    y_train
)


# ============================================================
# Evaluate
# ============================================================

predictions = model.predict(
    X_test
)

accuracy = accuracy_score(
    y_test,
    predictions
)

print("\n==============================")
print("MODEL RESULTS")
print("==============================")

print(
    f"Accuracy: {accuracy * 100:.2f}%"
)

print("\nClassification Report:\n")

print(
    classification_report(
        y_test,
        predictions,
        zero_division=0
    )
)


# ============================================================
# Save model
# ============================================================

joblib.dump(
    model,
    MODEL_FILE
)

print("\nModel saved successfully:")

print(MODEL_FILE)