import os

from dotenv import load_dotenv
from google import genai


# Load variables from .env
load_dotenv()

# Read Gemini API key
api_key = os.getenv("GEMINI_API_KEY")

if not api_key:
    raise ValueError(
        "GEMINI_API_KEY is missing from .env"
    )


# Create Gemini client
client = genai.Client(
    api_key=api_key
)


def generate_farming_answer(
    question: str,
    context: str
) -> str:
    """
    Generates a simple farmer-friendly answer
    using the retrieved agriculture context.
    """

    prompt = f"""
You are WeatherRoots AI, an agricultural assistant for farmers.

Your task is to answer the farmer's question using only
the agriculture context provided below.

Rules:
- Use the provided agriculture context as the main source of facts.
- Do not invent farming information.
- Keep the answer simple and practical.
- Keep the answer concise because it may be spoken aloud.
- If the provided context is insufficient, clearly say so.
- Do not mention FAISS, RAG, embeddings, vector databases,
  prompts, models, or internal AI systems.
- Do not assume the farmer's location, season, weather,
  water availability, or soil condition unless provided.
- Avoid unsafe fertilizer or pesticide recommendations.

Farmer Question:
{question}

Agriculture Context:
{context}

Give a clear and useful farming answer.
"""

    try:
        print("Sending request to Gemini...")

        response = client.interactions.create(
            model="gemini-3.7-flash",
            input=prompt
        )

        # Interactions API uses output_text
        answer = response.output_text

        if not answer:
            return (
                "I could not generate a farming answer "
                "from the available information."
            )

        return answer.strip()

    except Exception as error:
        print("Gemini API Error:", error)

        return (
            "The AI farming service is currently unavailable."
        )