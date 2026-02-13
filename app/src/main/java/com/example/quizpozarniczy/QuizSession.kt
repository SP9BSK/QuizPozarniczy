package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.Question

object QuizSession {
    var totalPlayers = 1
    var currentPlayer = 1

    val playerNames = mutableListOf<String>()
    val results = mutableListOf<PlayerResult>()

    // ✅ DODANE: lista pytań dla quizu
    var questions = mutableListOf<Question>()

    // Reset sesji i nadanie domyślnych nazw
    fun reset(players: Int) {
        totalPlayers = players
        currentPlayer = 1
        results.clear()

        playerNames.clear()
        repeat(players) {
            playerNames.add("Zawodnik ${it + 1}")
        }

        questions.clear()
    }

    // Upewnij się, że lista graczy ma dokładnie "count" elementów
    fun ensurePlayers(count: Int) {
        while (playerNames.size < count) {
            playerNames.add("Zawodnik ${playerNames.size + 1}")
        }
        if (playerNames.size > count) {
            playerNames.subList(count, playerNames.size).clear()
        }
        totalPlayers = count
    }

    fun resetAll() {
        totalPlayers = 1
        currentPlayer = 1
        playerNames.clear()
        results.clear()
        questions.clear()
    }
}
