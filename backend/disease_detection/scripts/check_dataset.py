from datasets import load_dataset


print("Loading PlantVillage...")

dataset = load_dataset(
    "mohanty/PlantVillage"
)

print("\nDataset:")
print(dataset)

print("\nAvailable splits:")
print(dataset.keys())


for split_name in dataset.keys():

    split = dataset[split_name]

    print(
        f"\n=============================="
    )
    print(
        f"Split: {split_name}"
    )
    print(
        f"Rows: {len(split)}"
    )

    print(
        "\nFeatures:"
    )
    print(
        split.features
    )

    print(
        "\nColumn names:"
    )
    print(
        split.column_names
    )

    print(
        "\nFirst example:"
    )

    sample = split[0]

    for key, value in sample.items():

        if key == "image":
            print(
                f"{key}: {value}"
            )
        else:
            print(
                f"{key}: {value}"
            )
            from datasets import load_dataset


print("Loading PlantVillage...")

dataset = load_dataset(
    "mohanty/PlantVillage"
)

print("\nDataset:")
print(dataset)

print("\nAvailable splits:")
print(dataset.keys())


for split_name in dataset.keys():

    split = dataset[split_name]

    print(
        f"\n=============================="
    )
    print(
        f"Split: {split_name}"
    )
    print(
        f"Rows: {len(split)}"
    )

    print(
        "\nFeatures:"
    )
    print(
        split.features
    )

    print(
        "\nColumn names:"
    )
    print(
        split.column_names
    )

    print(
        "\nFirst example:"
    )

    sample = split[0]

    for key, value in sample.items():

        if key == "image":
            print(
                f"{key}: {value}"
            )
        else:
            print(
                f"{key}: {value}"
            )