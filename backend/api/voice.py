from fastapi import APIRouter, HTTPException
from pydantic import BaseModel

from services.language_detector import detect_language
from services.translator import (
    translate_to_english,
    translate_from_english
)
from services.ai_engine import generate_response


# ============================================================
# Router
# ============================================================

router = APIRouter(
    prefix="/voice",
    tags=["Voice Assistant"]
)


# ============================================================
# Request Model
# ============================================================

class VoiceRequest(BaseModel):
    question: str


# ============================================================
# Response Model
# ============================================================

class VoiceResponse(BaseModel):
    detected_language: str
    original_question: str
    english_question: str
    english_response: str
    response: str


# ============================================================
# Fallback Answer
# ============================================================

def build_fallback_answer(
    english_question: str
) -> str:

    question_lower = (
        english_question
        .strip()
        .lower()
    )

    if (
        "black soil" in question_lower
        or "black cotton soil" in question_lower
    ):
        return (
            "Black soil is generally suitable for crops such as "
            "cotton, soybean, wheat, groundnut and some pulses. "
            "The final crop choice should also consider rainfall, "
            "water availability, season and local soil conditions."
        )

    if "rice" in question_lower:
        return (
            "Rice generally grows well where sufficient water, "
            "warm conditions and suitable soil are available. "
            "Local rainfall, irrigation and soil type should be "
            "checked before planting."
        )

    if (
        "irrigation" in question_lower
        or "water" in question_lower
    ):
        return (
            "Irrigation needs depend on the crop, soil type, "
            "temperature, rainfall and growth stage. "
            "Check soil moisture before irrigating and avoid "
            "both overwatering and severe water stress."
        )

    return (
        "WeatherRoots could not generate the AI response right now. "
        "Please try the farming question again shortly."
    )


# ============================================================
# Voice Query Endpoint
# ============================================================

@router.post(
    "/query",
    response_model=VoiceResponse
)
def process_voice_query(
    request: VoiceRequest
):

    try:

        # ----------------------------------------------------
        # STEP 1
        # Clean farmer input
        # ----------------------------------------------------

        original_question = (
            request.question.strip()
        )

        if not original_question:

            raise HTTPException(
                status_code=400,
                detail="Question cannot be empty."
            )


        # ----------------------------------------------------
        # STEP 2
        # Detect language
        # ----------------------------------------------------

        language = detect_language(
            original_question
        )


        if not language:

            language = "en"


        language = (
            str(language)
            .strip()
            .lower()
        )


        print("\n======================================")
        print("WeatherRoots AI Voice Assistant")
        print("======================================")

        print("\nOriginal Question:")
        print(original_question)

        print("\nDetected Language:")
        print(language)


        # ----------------------------------------------------
        # STEP 3
        # Translate question to English
        # ----------------------------------------------------

        if language == "en":

            english_question = (
                original_question
            )

        else:

            english_question = (
                translate_to_english(
                    original_question,
                    language
                )
            )


        # Safety check
        if not english_question:

            print(
                "\nTranslation returned empty text."
            )

            english_question = (
                original_question
            )


        english_question = (
            str(english_question)
            .strip()
        )


        print("\nEnglish Question:")
        print(english_question)


        # ----------------------------------------------------
        # STEP 4
        # RAG + Gemini response
        # ----------------------------------------------------

        english_answer = (
            generate_response(
                english_question
            )
        )


        # ----------------------------------------------------
        # STEP 5
        # Never allow empty AI response
        # ----------------------------------------------------

        if not english_answer:

            print(
                "\nAI engine returned an empty response."
            )

            english_answer = (
                build_fallback_answer(
                    english_question
                )
            )


        english_answer = (
            str(english_answer)
            .strip()
        )


        if not english_answer:

            english_answer = (
                "WeatherRoots could not generate a "
                "farming answer right now."
            )


        print("\nEnglish AI Response:")
        print(english_answer)


        # ----------------------------------------------------
        # STEP 6
        # Translate response back to farmer language
        # ----------------------------------------------------

        if language == "en":

            final_answer = (
                english_answer
            )

        else:

            translated_answer = (
                translate_from_english(
                    english_answer,
                    language
                )
            )


            # If translation fails, at least return English
            if translated_answer:

                final_answer = (
                    str(translated_answer)
                    .strip()
                )

            else:

                print(
                    "\nResponse translation failed. "
                    "Returning English answer."
                )

                final_answer = (
                    english_answer
                )


        # ----------------------------------------------------
        # STEP 7
        # Final safety check
        # ----------------------------------------------------

        if not final_answer:

            final_answer = (
                english_answer
            )


        print("\nFinal Farmer Response:")
        print(final_answer)

        print("\n======================================\n")


        # ----------------------------------------------------
        # STEP 8
        # Return structured response
        # ----------------------------------------------------

        return VoiceResponse(

            detected_language=(
                language
            ),

            original_question=(
                original_question
            ),

            english_question=(
                english_question
            ),

            english_response=(
                english_answer
            ),

            response=(
                final_answer
            )
        )


    except HTTPException:

        raise


    except Exception as error:

        print("\nVoice API Error:")
        print(
            repr(error)
        )

        raise HTTPException(
            status_code=500,
            detail=(
                "WeatherRoots AI could not "
                "process the question."
            )
        )