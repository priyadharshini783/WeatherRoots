import os

from dotenv import load_dotenv
from google import genai


load_dotenv()

api_key = os.getenv("GEMINI_API_KEY")

if not api_key:
    raise ValueError("GEMINI_API_KEY is missing from .env")


client = genai.Client(api_key=api_key)


def generate_farming_answer(question: str, context: str) -> str:

    prompt = f"""
You are WeatherRoots AI, an agricultural assistant for farmers.

Answer the farmer's question using the agricultural context provided.

Rules:
- Use the context as your main factual source.
- Do not invent farming information.
- Keep the answer simple and practical.
- Keep the response concise because it may be spoken aloud.
- If the provided context is insufficient, clearly say so.
- Do not mention FAISS, RAG, embeddings, or internal AI systems.

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

        if not response.output_text:
            return "I could not generate a farming answer."

        return response.output_text.strip()

    except Exception as error:

        print("Gemini API Error:", error)

        return "The AI farming service is currently unavailable."
