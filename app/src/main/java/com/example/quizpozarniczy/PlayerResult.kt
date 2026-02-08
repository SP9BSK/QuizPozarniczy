package com.example.quizpozarniczy.model

import com.example.quizpozarniczy.model.WrongAnswer

data class PlayerResult(
    val playerNumber: Int,
    val score: Int,
    val total: Int,
    val timeSeconds: Int,
    val wrongAnswers: List<WrongAnswer>
)
