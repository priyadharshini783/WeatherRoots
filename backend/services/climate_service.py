from pathlib import Path

import pandas as pd


# ============================================================
# File paths
# ============================================================

BASE_DIR = Path(__file__).resolve().parent.parent

RAINFALL_FILE = (
    BASE_DIR
    / "data"
    / "raw"
    / "Sub_Division_IMD_2017.csv"
)


# ============================================================
# Historical Tamil Nadu annual rainfall
# ============================================================

def get_tamil_nadu_annual_rainfall() -> float:
    """
    Read the IMD rainfall CSV and return the historical
    average annual rainfall for Tamil Nadu in millimetres.
    """

    # --------------------------------------------------------
    # Check file exists
    # --------------------------------------------------------

    if not RAINFALL_FILE.exists():
        raise FileNotFoundError(
            f"Rainfall dataset not found: {RAINFALL_FILE}"
        )

    # --------------------------------------------------------
    # Load rainfall data
    # --------------------------------------------------------

    rainfall_df = pd.read_csv(
        RAINFALL_FILE
    )

    # --------------------------------------------------------
    # Validate required columns
    # --------------------------------------------------------

    required_columns = [
        "SUBDIVISION",
        "ANNUAL"
    ]

    missing_columns = [
        column
        for column in required_columns
        if column not in rainfall_df.columns
    ]

    if missing_columns:
        raise ValueError(
            f"Missing rainfall columns: {missing_columns}"
        )

    # --------------------------------------------------------
    # Select Tamil Nadu rows
    # --------------------------------------------------------

    tamil_nadu_df = rainfall_df[
        rainfall_df["SUBDIVISION"]
        .astype(str)
        .str.strip()
        .str.lower()
        == "tamil nadu"
    ].copy()

    if tamil_nadu_df.empty:
        raise ValueError(
            "Tamil Nadu rainfall records were not found "
            "in the IMD dataset."
        )

    # --------------------------------------------------------
    # Convert annual rainfall to numeric
    # --------------------------------------------------------

    tamil_nadu_df["ANNUAL"] = pd.to_numeric(
        tamil_nadu_df["ANNUAL"],
        errors="coerce"
    )

    tamil_nadu_df = tamil_nadu_df.dropna(
        subset=["ANNUAL"]
    )

    if tamil_nadu_df.empty:
        raise ValueError(
            "Tamil Nadu rainfall records do not contain "
            "valid annual rainfall values."
        )

    # --------------------------------------------------------
    # Historical average annual rainfall
    # --------------------------------------------------------

    average_annual_rainfall = float(
        tamil_nadu_df["ANNUAL"].mean()
    )

    return round(
        average_annual_rainfall,
        2
    )