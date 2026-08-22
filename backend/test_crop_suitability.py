from services.crop_suitability import recommend_crops


results = recommend_crops(
    temperature=30,
    humidity=70,
    rainfall=900,
    soil_type="Black",
    water_availability="Medium",
    season="Kharif"
)


print("\nWeatherRoots Crop Suitability Results\n")

for item in results:

    print(
        item["crop"],
        "-",
        item["score"],
        "%"
    )

    print(
        item["details"]
    )

    print()