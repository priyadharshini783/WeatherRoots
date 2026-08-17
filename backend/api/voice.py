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
# Voice Query Endpoint
# ============================================================

@router.post(
    "/query",
    response_model=VoiceResponse
)
def process_voice_query(request: VoiceRequest):

    try:

        # ----------------------------------------------------
        # Step 1: Clean input
        # ----------------------------------------------------

        original_question = request.question.strip()

        if not original_question:
            raise HTTPException(
                status_code=400,
                detail="Question cannot be empty."
            )


        # ----------------------------------------------------
        # Step 2: Detect farmer language
        # ----------------------------------------------------

        language = detect_language(
            original_question
        )

        print("\n======================================")
        print("WeatherRoots AI Voice Assistant")
        print("======================================")

        print("\nOriginal Question:")
        print(original_question)

        print("\nDetected Language:")
        print(language)


        # ----------------------------------------------------
        # Step 3: Translate farmer question to English
        # ----------------------------------------------------

        if language == "en":

            english_question = original_question

        else:

            english_question = translate_to_english(
                original_question,
                language
            )


        print("\nEnglish Question:")
        print(english_question)


        # ----------------------------------------------------
        # Step 4: Send English question to RAG + Gemini
        # ----------------------------------------------------

        english_answer = generate_response(
            english_question
        )


        print("\nEnglish AI Response:")
        print(english_answer)


        # ----------------------------------------------------
        # Step 5: Translate AI response back to farmer language
        # ----------------------------------------------------

        if language == "en":

            final_answer = english_answer

        else:

            final_answer = translate_from_english(
                english_answer,
                language
            )


        print("\nFinal Farmer Response:")
        print(final_answer)

        print("\n======================================\n")


        # ----------------------------------------------------
        # Step 6: Return response to Android / API client
        # ----------------------------------------------------

        return VoiceResponse(

            detected_language=language,

            original_question=original_question,

            english_question=english_question,

            english_response=english_answer,

            response=final_answer
        )


    except HTTPException:
        raise


    except Exception as error:

        print("\nVoice API Error:")
        print(error)

        raise HTTPException(
            status_code=500,
            detail="WeatherRoots AI could not process the question."
        )