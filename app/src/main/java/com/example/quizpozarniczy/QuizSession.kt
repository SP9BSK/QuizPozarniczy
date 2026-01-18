package com.example.quizpozarniczy

object QuizSession {
    var totalPlayers = 1
    var currentPlayer = 1
    val results = mutableListOf<PlayerResult>()

    fun reset() {
        currentPlayer = 1
        results.clear()
    }
}
