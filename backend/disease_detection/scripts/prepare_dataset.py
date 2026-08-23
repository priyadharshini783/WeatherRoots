from pathlib import Path
import random
import shutil

from datasets import load_dataset


# ============================================================
# Configuration
# ============================================================

BASE_DIR = Path(__file__).resolve().parent.parent

DATA_DIR = BASE_DIR / "data"

TRAIN_DIR = DATA_DIR / "train"
VAL_DIR = DATA_DIR / "val"
TEST_DIR = DATA_DIR / "test"

RANDOM_SEED = 42

random.seed(RANDOM_SEED)


# ============================================================
# WeatherRoots supported disease classes
# ============================================================

SELECTED_CLASSES = [
    "Tomato___healthy",
    "Tomato___Early_blight",
    "Tomato___Late_blight",
    "Tomato___Leaf_Mold",
    "Tomato___Septoria_leaf_spot",

    "Potato___healthy",
    "Potato___Early_blight",
    "Potato___Late_blight",

    "Pepper,_bell___healthy",
    "Pepper,_bell___Bacterial_spot",

    "Corn_(maize)___healthy",
    "Corn_(maize)___Common_rust_",
    "Corn_(maize)___Northern_Leaf_Blight",
    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot",
]


# ============================================================
# Safe folder names
# ============================================================

def safe_folder_name(class_name: str) -> str:

    return (
        class_name
        .replace(" ", "_")
        .replace(",", "")
        .replace("(", "")
        .replace(")", "")
    )


# ============================================================
# Create output folders
# ============================================================

def prepare_directories():

    for directory in [
        TRAIN_DIR,
        VAL_DIR,
        TEST_DIR
    ]:

        directory.mkdir(
            parents=True,
            exist_ok=True
        )


# ============================================================
# Save image
# ============================================================

def save_image(
    image,
    class_name,
    split,
    index
):

    folder_name = safe_folder_name(
        class_name
    )

    output_dir = (
        DATA_DIR
        / split
        / folder_name
    )

    output_dir.mkdir(
        parents=True,
        exist_ok=True
    )

    image = image.convert("RGB")

    output_file = (
        output_dir
        / f"{index:06d}.jpg"
    )

    image.save(
        output_file,
        format="JPEG",
        quality=95
    )


# ============================================================
# Main
# ============================================================

def main():

    print(
        "Loading PlantVillage dataset..."
    )

    dataset = load_dataset(
        "mohanty/PlantVillage",
        
    )

    print(dataset)

    label_names = (
        dataset["train"]
        .features["label"]
        .names
    )

    label_to_id = {
        name: index
        for index, name
        in enumerate(label_names)
    }


    print("\nSelected WeatherRoots classes:")

    for class_name in SELECTED_CLASSES:

        if class_name not in label_to_id:

            raise ValueError(
                f"PlantVillage class not found: "
                f"{class_name}"
            )

        print(
            f"✓ {class_name}"
        )


    selected_ids = {
        label_to_id[name]
        for name in SELECTED_CLASSES
    }


    # --------------------------------------------------------
    # Filter official train/test datasets
    # --------------------------------------------------------

    print(
        "\nFiltering training images..."
    )

    train_full = dataset["train"].filter(
        lambda row:
        row["label"] in selected_ids
    )


    print(
        "Filtering test images..."
    )

    test_data = dataset["test"].filter(
        lambda row:
        row["label"] in selected_ids
    )


    # --------------------------------------------------------
    # Create validation split by LEAF ID
    #
    # This avoids putting images of the same physical leaf
    # into both training and validation.
    # --------------------------------------------------------

    print(
        "\nCreating validation split..."
    )

    leaf_ids = list(
        set(
            train_full["leaf_id"]
        )
    )

    random.shuffle(
        leaf_ids
    )

    validation_count = int(
        len(leaf_ids) * 0.15
    )

    validation_leaf_ids = set(
        leaf_ids[:validation_count]
    )


    train_data = train_full.filter(
        lambda row:
        row["leaf_id"]
        not in validation_leaf_ids
    )


    val_data = train_full.filter(
        lambda row:
        row["leaf_id"]
        in validation_leaf_ids
    )


    print(
        f"\nTraining images: "
        f"{len(train_data)}"
    )

    print(
        f"Validation images: "
        f"{len(val_data)}"
    )

    print(
        f"Test images: "
        f"{len(test_data)}"
    )


    # --------------------------------------------------------
    # Remove old prepared folders
    # --------------------------------------------------------

    for directory in [
        TRAIN_DIR,
        VAL_DIR,
        TEST_DIR
    ]:

        if directory.exists():

            shutil.rmtree(
                directory
            )


    prepare_directories()


    # --------------------------------------------------------
    # Export images
    # --------------------------------------------------------

    split_datasets = {
        "train": train_data,
        "val": val_data,
        "test": test_data
    }


    for split_name, split_data in (
        split_datasets.items()
    ):

        print(
            f"\nExporting {split_name}..."
        )

        for index, row in enumerate(
            split_data
        ):

            class_name = label_names[
                row["label"]
            ]

            save_image(
                image=row["image"],
                class_name=class_name,
                split=split_name,
                index=index
            )

            if (
                index > 0
                and index % 1000 == 0
            ):

                print(
                    f"{index} images exported..."
                )


    print(
        "\n=================================="
    )

    print(
        "WeatherRoots Disease Dataset Ready"
    )

    print(
        "=================================="
    )

    print(
        f"Location: {DATA_DIR}"
    )


if __name__ == "__main__":

    main()