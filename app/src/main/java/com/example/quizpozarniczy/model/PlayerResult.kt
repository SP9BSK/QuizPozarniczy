package com.example.quizpozarniczy.model

data class PlayerResult(
    val playerNumber: Int,
    val score: Int,
    val total: Int,
    val wrongAnswers: List<WrongAnswer>
)

