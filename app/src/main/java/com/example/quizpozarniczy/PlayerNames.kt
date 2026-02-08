package com.example.quizpozarniczy

object PlayerNames {
    var names: MutableList<String> = mutableListOf()

    fun ensureSize(count: Int) {
        while (names.size < count) {
            names.add("Zawodnik ${names.size + 1}")
        }
        if (names.size > count) {
            names = names.take(count).toMutableList()
        }
    }
}
