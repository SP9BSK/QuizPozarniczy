package com.example.quizpozarniczy.model

data class LocalQuestion(
    val id: Int,
    val prefix: String,
    var quotedValue: String?,   // 🔥 NULL = brak edycji
    val suffix: String,
    val answers: MutableList<String>,
    var correctIndex: Int
) {
    fun fullQuestion(): String {
        return if (quotedValue != null)
            "$prefix „$quotedValue”$suffix"
        else
            prefix
    }
}
