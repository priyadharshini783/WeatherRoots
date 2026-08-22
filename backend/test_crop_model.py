from services.crop_predictor import predict_crop


print("Testing WeatherRoots Crop Recommendation Model...\n")


result = predict_crop(
    temperature=30,
    humidity=70,
    rainfall=120,
    soil_type="Black",
    water_availability="Medium",
    previous_crop="Cotton",
    season="Kharif"
)


print("Crop Recommendation Result:")
print(result)

print("\nRecommended Crop:")
print(result["recommended_crop"])

print("\nConfidence:")
print(result["confidence"])