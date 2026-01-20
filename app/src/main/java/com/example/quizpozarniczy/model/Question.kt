package com.example.quizpozarniczy.model

data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
)
