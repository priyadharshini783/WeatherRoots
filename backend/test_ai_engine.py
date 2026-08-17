from services.ai_engine import generate_response


question = "Which crop is suitable for black soil?"

print("Testing WeatherRoots RAG + LLM...\n")

answer = generate_response(question)

print("\nFinal Answer:")
print(answer)