package com.example.quizpozarniczy

object QuizSession {
    var totalPlayers = 1
    var currentPlayer = 1

    val playerNames = mutableListOf<String>()
    val results = mutableListOf<PlayerResult>()

    fun reset(players: Int) {
        totalPlayers = players
        currentPlayer = 1
        results.clear()

        playerNames.clear()
        repeat(players) {
            playerNames.add("Zawodnik ${it + 1}")
        }
    }
}
