package com.example.quizpozarniczy.model

data class LocalQuestion(
    val id: Int,
    val question: String,
    val answers: MutableList<String>,
    var correctIndex: Int
)
