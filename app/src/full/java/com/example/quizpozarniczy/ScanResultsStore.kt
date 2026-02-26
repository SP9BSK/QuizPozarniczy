package com.example.quizpozarniczy

object ScanResultsStore {
    private val results = mutableListOf<String>()

    fun add(value: String) {
        results.add(value)
    }

    fun getAll(): List<String> = results

    fun clear() {
        results.clear()
    }
}
