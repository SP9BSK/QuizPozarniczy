package com.example.quizpozarniczy

data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
)
