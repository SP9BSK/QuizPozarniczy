package com.example.quizpozarniczy.model

data class PlayerResult(
    val playerNumber: Int,
    val score: Int,
    val total: Int,
    val wrongAnswers: List<WrongAnswer>
)

data class WrongAnswer(
    val question: String,
    val chosenAnswer: String,
    val correctAnswer: String
)
