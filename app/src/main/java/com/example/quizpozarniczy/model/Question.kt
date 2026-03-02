package com.example.quizpozarniczy.model

data class Question(
    val id: Int = 0,
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
    val imageResId: Int? = null
)
