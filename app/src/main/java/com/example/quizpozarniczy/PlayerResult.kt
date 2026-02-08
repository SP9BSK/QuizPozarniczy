package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.WrongAnswer

data class PlayerResult(
    val playerNumber: Int,
    val playerName: String,
    val score: Int,
    val total: Int,
    val timeSeconds: Int,
    val wrongAnswers: List<WrongAnswer>
)
