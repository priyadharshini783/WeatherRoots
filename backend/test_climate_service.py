from services.climate_service import (
    get_tamil_nadu_annual_rainfall
)


rainfall = get_tamil_nadu_annual_rainfall()

print(
    "Tamil Nadu Historical Average Annual Rainfall:"
)

print(
    f"{rainfall} mm"
)