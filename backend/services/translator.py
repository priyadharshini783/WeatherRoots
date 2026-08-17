from deep_translator import GoogleTranslator


def translate_to_english(
    text: str,
    source_language: str
) -> str:
    """
    Translate farmer input into English.
    """

    try:
        if source_language == "en":
            return text

        translated_text = GoogleTranslator(
            source=source_language,
            target="en"
        ).translate(text)

        return translated_text

    except Exception as error:
        print("Translation to English Error:", error)
        return text


def translate_from_english(
    text: str,
    target_language: str
) -> str:
    """
    Translate English AI response back
    to the farmer's original language.
    """

    try:
        if target_language == "en":
            return text

        translated_text = GoogleTranslator(
            source="en",
            target=target_language
        ).translate(text)

        return translated_text

    except Exception as error:
        print("Translation from English Error:", error)
        return text