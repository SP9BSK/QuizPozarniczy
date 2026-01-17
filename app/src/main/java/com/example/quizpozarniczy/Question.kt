package com.example.quizpozarniczy

data class Question(
    val question: String,
    val answers: List<String>,
    val correct: Int
)
