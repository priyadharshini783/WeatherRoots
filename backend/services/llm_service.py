import os

from dotenv import load_dotenv
from google import genai


# ============================================================
# Load environment variables
# ============================================================

load_dotenv()

api_key = os.getenv("GEMINI_API_KEY")

if not api_key:
    raise ValueError(
        "GEMINI_API_KEY is missing from .env"
    )


# ============================================================
# Gemini client
# ============================================================

client = genai.Client(
    api_key=api_key
)


# ============================================================
# Generate farming answer
# ============================================================

def generate_farming_answer(
    question: str,
    context: str
) -> str:

    prompt = f"""
You are WeatherRoots AI, an agricultural assistant for farmers.

Answer the farmer's question using the agricultural context provided.

Rules:
- Use the provided context as the main factual source.
- Do not invent farming information.
- Keep the answer simple and practical.
- Keep the response concise because it may be spoken aloud.
- If the provided context is insufficient, clearly say so.
- Do not mention FAISS, RAG, embeddings, vector databases,
  or internal AI systems.
- Do not guarantee agricultural success.
- Avoid exact fertilizer or pesticide dosages unless they are
  explicitly present in the supplied context.

Farmer Question:
{question}

Agriculture Context:
{context}

Answer:
"""

    try:

        response = client.interactions.create(
            model="gemini-3.7-flash",
            input=prompt
        )

        # ----------------------------------------------------
        # Safely extract generated text
        # ----------------------------------------------------

        output_text = getattr(
            response,
            "output_text",
            None
        )

        if not output_text:

            print(
                "Gemini returned an empty response."
            )

            return ""

        return output_text.strip()


    except Exception as error:

        error_text = str(error)

        print(
            "Gemini API Error:",
            error
        )

        # ----------------------------------------------------
        # Handle quota / rate-limit problems
        #
        # Returning an empty string allows crop_advice.py
        # to generate its deterministic fallback explanation.
        # ----------------------------------------------------

        if (
            "429" in error_text
            or "too_many_requests" in error_text.lower()
            or "quota exceeded" in error_text.lower()
            or "rate limit" in error_text.lower()
        ):

            print(
                "Gemini quota/rate limit reached. "
                "Using WeatherRoots fallback response."
            )

            return ""


        # ----------------------------------------------------
        # Temporary Gemini service failure
        # ----------------------------------------------------

        if (
            "503" in error_text
            or "unavailable" in error_text.lower()
            or "high demand" in error_text.lower()
        ):

            print(
                "Gemini is temporarily unavailable. "
                "Using WeatherRoots fallback response."
            )

            return ""


        # ----------------------------------------------------
        # Any other Gemini error
        # ----------------------------------------------------

        print(
            "Unexpected Gemini error. "
            "Using WeatherRoots fallback response."
        )

        return ""