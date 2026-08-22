from pathlib import Path

import pandas as pd


# ============================================================
# Paths
# ============================================================

BASE_DIR = Path(__file__).resolve().parent.parent

RAW_DIR = BASE_DIR / "data" / "raw"
PROCESSED_DIR = BASE_DIR / "data" / "processed"

CROP_FILE = RAW_DIR / "crop_production_synthetic.csv"
RAINFALL_FILE = RAW_DIR / "Sub_Division_IMD_2017.csv"
SOIL_MOISTURE_FILE = RAW_DIR / "sm_Tamilnadu_2020.csv"

OUTPUT_FILE = (
    PROCESSED_DIR /
    "weatherroots_crop_dataset.csv"
)


# ============================================================
# Load crop production data
# ============================================================

def load_crop_data():

    print("Loading crop production data...")

    crop_df = pd.read_csv(CROP_FILE)

    crop_df["district"] = (
        crop_df["district"]
        .astype(str)
        .str.strip()
        .str.upper()
    )

    crop_df["season"] = (
        crop_df["season"]
        .astype(str)
        .str.strip()
        .str.title()
    )

    crop_df["crop"] = (
        crop_df["crop"]
        .astype(str)
        .str.strip()
        .str.title()
    )

    print(
        f"Crop rows loaded: {len(crop_df)}"
    )

    return crop_df


# ============================================================
# Prepare Tamil Nadu rainfall baseline
# ============================================================

def prepare_rainfall_data():

    print("Loading IMD rainfall data...")

    rainfall_df = pd.read_csv(
        RAINFALL_FILE
    )

    tamil_nadu = rainfall_df[
        rainfall_df["SUBDIVISION"]
        .astype(str)
        .str.strip()
        .str.lower()
        == "tamil nadu"
    ].copy()

    if tamil_nadu.empty:
        raise ValueError(
            "Tamil Nadu rainfall data was not found."
        )

    # Historical rainfall averages.
    #
    # MAM  = March-April-May
    # JJAS = June-July-August-September
    # OND  = October-November-December

    rainfall_baseline = {

        "Kharif": float(
            tamil_nadu["JJAS"].mean()
        ),

        "Rabi": float(
            tamil_nadu["OND"].mean()
        ),

        "Summer": float(
            tamil_nadu["MAM"].mean()
        )
    }

    print(
        "Historical rainfall baseline:"
    )

    for season, value in (
        rainfall_baseline.items()
    ):
        print(
            f"{season}: {value:.2f} mm"
        )

    return rainfall_baseline


# ============================================================
# Prepare district soil moisture
# ============================================================

def prepare_soil_moisture():

    print("Loading Tamil Nadu soil moisture data...")

    soil_df = pd.read_csv(
        SOIL_MOISTURE_FILE
    )

    soil_df["DistrictName"] = (
        soil_df["DistrictName"]
        .astype(str)
        .str.strip()
        .str.upper()
    )

    moisture_column = (
        "Volume Soilmoisture "
        "percentage (at 15cm)"
    )

    soil_df[moisture_column] = (
        pd.to_numeric(
            soil_df[moisture_column],
            errors="coerce"
        )
    )

    # Average moisture for each district
    district_moisture = (
        soil_df
        .groupby("DistrictName")[
            moisture_column
        ]
        .mean()
        .reset_index()
    )

    district_moisture.columns = [
        "district",
        "soil_moisture"
    ]

    # --------------------------------------------------------
    # Convert numeric moisture into Low / Medium / High
    #
    # Quantiles are used instead of inventing arbitrary
    # agriculture thresholds.
    # --------------------------------------------------------

    low_limit = (
        district_moisture[
            "soil_moisture"
        ].quantile(0.33)
    )

    high_limit = (
        district_moisture[
            "soil_moisture"
        ].quantile(0.66)
    )

    def classify_water(value):

        if pd.isna(value):
            return "Unknown"

        if value <= low_limit:
            return "Low"

        if value <= high_limit:
            return "Medium"

        return "High"

    district_moisture[
        "water_availability"
    ] = (
        district_moisture[
            "soil_moisture"
        ].apply(classify_water)
    )

    print(
        f"Low threshold: {low_limit:.2f}"
    )

    print(
        f"High threshold: {high_limit:.2f}"
    )

    print(
        f"Districts with moisture data: "
        f"{len(district_moisture)}"
    )

    return district_moisture


# ============================================================
# Create processed dataset
# ============================================================

def create_dataset():

    crop_df = load_crop_data()

    rainfall_baseline = (
        prepare_rainfall_data()
    )

    district_moisture = (
        prepare_soil_moisture()
    )

    # --------------------------------------------------------
    # Add rainfall based on crop season
    # --------------------------------------------------------

    crop_df["rainfall"] = (
        crop_df["season"]
        .map(rainfall_baseline)
    )

    # --------------------------------------------------------
    # Merge district moisture information
    # --------------------------------------------------------

    final_df = crop_df.merge(
        district_moisture,
        how="left",
        on="district"
    )

    # --------------------------------------------------------
    # If a district name mismatch exists, preserve the row
    # but mark water availability as Unknown.
    # --------------------------------------------------------

    final_df[
        "water_availability"
    ] = (
        final_df[
            "water_availability"
        ].fillna("Unknown")
    )

    # --------------------------------------------------------
    # Target column
    # --------------------------------------------------------

    final_df[
        "recommended_crop"
    ] = final_df["crop"]

    # --------------------------------------------------------
    # Select useful WeatherRoots columns
    # --------------------------------------------------------

    final_columns = [

        "state",

        "district",

        "year",

        "season",

        "rainfall",

        "soil_moisture",

        "water_availability",

        "area_hectares",

        "production_tonnes",

        "yield_tonnes_per_hectare",

        "recommended_crop",

        "source_type"
    ]

    final_df = final_df[
        final_columns
    ]

    # --------------------------------------------------------
    # Save result
    # --------------------------------------------------------

    PROCESSED_DIR.mkdir(
        parents=True,
        exist_ok=True
    )

    final_df.to_csv(
        OUTPUT_FILE,
        index=False
    )

    print("\n================================")
    print("WeatherRoots Dataset Created")
    print("================================")

    print(
        f"Rows: {len(final_df)}"
    )

    print(
        f"Columns: {len(final_df.columns)}"
    )

    print(
        "\nSaved to:"
    )

    print(
        OUTPUT_FILE
    )

    print(
        "\nWater availability counts:"
    )

    print(
        final_df[
            "water_availability"
        ].value_counts()
    )

    print(
        "\nCrop counts:"
    )

    print(
        final_df[
            "recommended_crop"
        ].value_counts()
    )


if __name__ == "__main__":

    create_dataset()