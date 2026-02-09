package com.example.quizpozarniczy

object QuizSession {
    var totalPlayers = 1
    var currentPlayer = 1

    val playerNames = mutableListOf<String>()
    val results = mutableListOf<PlayerResult>()

    // Reset sesji i nadanie domyślnych nazw
    fun reset(players: Int) {
        totalPlayers = players
        currentPlayer = 1
        results.clear()

        playerNames.clear()
        repeat(players) {
            playerNames.add("Zawodnik ${it + 1}")
        }
    }

    // ✅ Nowa funkcja: upewnij się, że lista graczy ma dokładnie "count" elementów
    fun ensurePlayers(count: Int) {
        // Dodaj brakujące nazwiska domyślne
        while (playerNames.size < count) {
            playerNames.add("Zawodnik ${playerNames.size + 1}")
        }
        // Jeśli jest więcej niż potrzebujemy, obetnij listę
        if (playerNames.size > count) {
            playerNames.subList(count, playerNames.size).clear()
        }
        // Zaktualizuj totalPlayers
        totalPlayers = count
    }
    fun resetAll() {
    totalPlayers = 1
    currentPlayer = 1
    playerNames.clear()
    results.clear()
}

}
