package com.example.quizpozarniczy.model

import com.example.quizpozarniczy.model.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuizExport(
    val playerName: String,
    val questions: List<Question>,
    val timeSeconds: Int
)
