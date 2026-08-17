import faiss
import numpy as np

from services.embedding import create_embedding



with open(
    "data/agriculture_data.txt",
    "r"
) as file:

    documents = file.readlines()



embeddings = []


for doc in documents:

    embeddings.append(
        create_embedding(doc)
    )



embeddings = np.array(
    embeddings
).astype("float32")



dimension = embeddings.shape[1]


index = faiss.IndexFlatL2(
    dimension
)


index.add(
    embeddings
)




def search_knowledge(question):


    query_vector = create_embedding(
        question
    )


    query_vector = np.array(
        [query_vector]
    ).astype("float32")


    distance, result = index.search(
        query_vector,
        3
    )


    answers = []


    for i in result[0]:

        answers.append(
            documents[i]
        )


    return " ".join(answers)