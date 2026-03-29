package com.example.quizpozarniczy

object ScanResultsStore {
    private val results = LinkedHashSet<String>()

    fun add(value: String) {
        val clean = value.trim()
        results.add(clean)   // Set sam pilnuje braku duplikatów
    }

    fun getAll(): List<String> = results.toList()

    fun clear() {
        results.clear()
    }
}
