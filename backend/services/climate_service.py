from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent

RAINFALL_FILE = (
    BASE_DIR
    / "data"
    / "raw"
    / "Sub_Division_IMD_2017.csv"
)


def get_tamil_nadu_annual_rainfall() -> float:
    """
    Returns the historical average annual rainfall
    for Tamil Nadu using the IMD rainfall dataset.
    """

    if not RAINFALL_FILE.exists():
        raise FileNotFoundError(
            f"Rainfall dataset not found: {RAINFALL_FILE}"
        )

    rainfall_df = pd.read_csv(
        RAINFALL_FILE
    )

    # Select Tamil Nadu subdivision
    tamil_nadu_df = rainfall_df[
        rainfall_df["SUBDIVISION"]
        .astype(str)
        .str.strip()
        .str.lower()
        == "tamil nadu"
    ].copy()

    if tamil_nadu_df.empty:
        raise ValueError(
            "Tamil Nadu rainfall records were not found."
        )

    # Convert ANNUAL rainfall to numeric
    tamil_nadu_df["ANNUAL"] = pd.to_numeric(
        tamil_nadu_df["ANNUAL"],
        errors="coerce"
    )

    tamil_nadu_df = tamil_nadu_df.dropna(
        subset=["ANNUAL"]
    )

    if tamil_nadu_df.empty:
        raise ValueError(
            "Tamil Nadu annual rainfall values are missing."
        )

    annual_average = float(
        tamil_nadu_df["ANNUAL"].mean()
    )

    return round(
        annual_average,
        2
    )