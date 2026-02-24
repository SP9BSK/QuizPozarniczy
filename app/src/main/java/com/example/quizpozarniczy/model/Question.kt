package com.example.quizpozarniczy.model

data class Question(
    val id: Int,
    val text: String,
    val answers: List<String>,
    val correctIndex: Int,
    val isLocal: Boolean = false   // 🔥 NOWE POLE
)
