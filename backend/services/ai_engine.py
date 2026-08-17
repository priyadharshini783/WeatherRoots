from services.rag_engine import search_knowledge
from services.llm_service import generate_farming_answer


def generate_response(question: str) -> str:
    """
    WeatherRoots Phase 3 pipeline:
    Question -> FAISS/RAG -> Gemini -> Natural farming response
    """

    # Step 1: Retrieve relevant agricultural knowledge
    knowledge = search_knowledge(question)

    # Step 2: Check whether RAG returned useful information
    if not knowledge:
        return (
            "I could not find enough agricultural information "
            "to answer this question reliably."
        )

    # Step 3: Convert retrieved result into text if needed
    if isinstance(knowledge, list):
        context = "\n\n".join(
            str(item) for item in knowledge
        )
    else:
        context = str(knowledge)

    # Debug output
    print("\n==============================")
    print("WeatherRoots AI Engine")
    print("==============================")

    print("\nFarmer Question:")
    print(question)

    print("\nRetrieved Agriculture Context:")
    print(context)

    # Step 4: Send question + FAISS context to Gemini
    answer = generate_farming_answer(
        question=question,
        context=context
    )

    print("\nGemini Farming Answer:")
    print(answer)

    return answer