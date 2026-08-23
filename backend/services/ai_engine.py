from services.rag_engine import search_knowledge
from services.llm_service import generate_farming_answer


def build_ai_fallback(
    question: str,
    context: str
) -> str:
    """
    Deterministic fallback used when Gemini is unavailable
    or returns an empty response.
    """

    question_lower = question.strip().lower()

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
            "Rice generally grows well in warm conditions with "
            "adequate water availability. Soil type, rainfall and "
            "irrigation should be considered before planting."
        )

    if (
        "irrigation" in question_lower
        or "water" in question_lower
    ):
        return (
            "Irrigation requirements depend on the crop, soil type, "
            "temperature, rainfall and growth stage. Check soil "
            "moisture before irrigation and avoid both excess water "
            "and severe water stress."
        )

    if "soil" in question_lower:
        return (
            "Crop suitability depends on soil type, rainfall, "
            "temperature, irrigation availability and season. "
            "Please provide the soil type and crop you are considering "
            "for a more specific recommendation."
        )

    # If RAG has useful information, return a short context-based fallback
    if context.strip():
        return (
            "WeatherRoots found relevant agricultural information, "
            "but the AI explanation service is temporarily unavailable. "
            "Please try again shortly."
        )

    return (
        "I could not find enough agricultural information to answer "
        "this question reliably."
    )


def generate_response(
    question: str
) -> str:
    """
    WeatherRoots AI pipeline:

    Farmer Question
        -> RAG / FAISS retrieval
        -> Gemini
        -> Farming response

    If Gemini is unavailable, a deterministic fallback response
    is returned so Android never receives an empty answer.
    """

    # ============================================================
    # STEP 1
    # Clean question
    # ============================================================

    cleaned_question = question.strip()

    if not cleaned_question:
        return (
            "Please ask a farming question."
        )


    # ============================================================
    # STEP 2
    # Retrieve relevant agricultural knowledge
    # ============================================================

    try:

        knowledge = search_knowledge(
            cleaned_question
        )

    except Exception as error:

        print(
            "\nRAG Search Error:"
        )

        print(
            repr(error)
        )

        knowledge = None


    # ============================================================
    # STEP 3
    # Convert retrieved knowledge into text
    # ============================================================

    if not knowledge:

        context = ""

    elif isinstance(
        knowledge,
        list
    ):

        context = "\n\n".join(
            str(item)
            for item in knowledge
        )

    else:

        context = str(
            knowledge
        )


    # ============================================================
    # DEBUG OUTPUT
    # ============================================================

    print("\n==============================")
    print("WeatherRoots AI Engine")
    print("==============================")

    print("\nFarmer Question:")
    print(cleaned_question)

    print("\nRetrieved Agriculture Context:")

    if context:

        print(context)

    else:

        print(
            "No relevant RAG context found."
        )


    # ============================================================
    # STEP 4
    # If RAG has no context, return safe fallback
    # ============================================================

    if not context.strip():

        fallback = build_ai_fallback(
            question=cleaned_question,
            context=""
        )

        print("\nAI Engine Fallback:")
        print(fallback)

        return fallback


    # ============================================================
    # STEP 5
    # Send question + RAG context to Gemini
    # ============================================================

    try:

        answer = generate_farming_answer(
            question=cleaned_question,
            context=context
        )

    except Exception as error:

        print(
            "\nGemini Generation Error:"
        )

        print(
            repr(error)
        )

        answer = ""


    # ============================================================
    # STEP 6
    # Handle empty Gemini response
    # ============================================================

    if not answer or not answer.strip():

        print(
            "\nGemini returned an empty response."
        )

        fallback = build_ai_fallback(
            question=cleaned_question,
            context=context
        )

        print("\nUsing WeatherRoots fallback:")
        print(fallback)

        return fallback


    answer = answer.strip()


    print("\nGemini Farming Answer:")
    print(answer)


    # ============================================================
    # STEP 7
    # Return final answer
    # ============================================================

    return answer