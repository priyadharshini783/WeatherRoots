from services.llm_service import generate_farming_answer


question = "Which crop is suitable for black soil?"

context = """
Black soil has good moisture retention.

Suitable crops include:
- Cotton
- Soybean
- Wheat
- Sugarcane
"""


print("Starting Gemini test...")

answer = generate_farming_answer(
    question=question,
    context=context
)

print("\nQuestion:")
print(question)

print("\nAI Answer:")
print(answer)