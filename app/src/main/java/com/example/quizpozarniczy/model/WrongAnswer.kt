package com.example.quizpozarniczy.model

data class WrongAnswer(
    val question: String,
    val answers: List<String>,   // A / B / C dokładnie jak w teście
    val chosenIndex: Int,        // co zawodnik zaznaczył
    val correctIndex: Int        // poprawna odpowiedź
)
