package com.example.quizpozarniczy.model

data class LocalQuestion(
    val id: Int,
    val prefix: String,          // tekst PRZED cudzysłowem
    var quotedValue: String,     // 🔥 EDYTOWALNE
    val suffix: String,          // tekst PO cudzysłowie
    val answers: MutableList<String>,
    var correctIndex: Int
) {
    fun fullQuestion(): String {
        return "$prefix „$quotedValue”$suffix"
    }
}
