package com.example.quizpozarniczy.model

data class WrongAnswer(
    val question: String,
    val answers: List<String>,
    val chosenIndex: Int,
    val correctIndex: Int
)
