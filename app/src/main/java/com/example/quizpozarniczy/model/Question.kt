package com.example.quizpozarniczy.model

data class Question(
    val question: String,
    val answers: List<String>,
    val correctIndex: Int
)
