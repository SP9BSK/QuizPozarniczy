package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.Question

object QuizSession {
    var totalPlayers = 1
    var currentPlayer = 1

    val playerNames = mutableListOf<String>()
    val results = mutableListOf<PlayerResult>()

    // ✅ Lista pytań dla quizu
    var questions: MutableList<Question> = mutableListOf()

    // 🔹 Reset sesji i nadanie domyślnych nazw (do inicjalizacji zawodników)
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

    // 🔹 Upewnij się, że lista graczy ma dokładnie "count" elementów
    // ❗ Zachowuje już wpisane imiona
    fun ensurePlayers(count: Int) {
        while (playerNames.size < count) {
            playerNames.add("Zawodnik ${playerNames.size + 1}")
        }
        if (playerNames.size > count) {
            playerNames.subList(count, playerNames.size).clear()
        }
        totalPlayers = count
    }

    // 🔹 Reset startowy quizu (przed rozpoczęciem) – nie kasuje imion
    fun resetAll() {
        currentPlayer = 1
        results.clear()
        questions.clear()
    }

    // 🔹 Pełny reset turnieju – kasuje wszystko, w tym imiona zawodników
    fun resetTournament() {
        totalPlayers = 1
        currentPlayer = 1
        playerNames.clear()
        results.clear()
        questions.clear()
    }

    // 🔹 Pobranie nazwy aktualnego zawodnika (bez ryzyka crasha)
    fun getCurrentPlayerName(): String {
        return playerNames.getOrNull(currentPlayer - 1) ?: "Zawodnik $currentPlayer"
    }
}
