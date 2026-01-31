package com.example.quizpozarniczy.model

data class LocalQuestion(
    val id: Int,

    // tekst przed 1 cudzysłowem
    val prefix: String,

    // 🔥 1. edytowalny fragment (może być null)
    var quotedValue1: String? = null,

    // tekst między 1 a 2 cudzysłowem
    val middle: String = "",

    // 🔥 2. edytowalny fragment (może być null)
    var quotedValue2: String? = null,

    // tekst końcowy
    val suffix: String = "",

    val answers: MutableList<String>,
    var correctIndex: Int
) {

    fun fullQuestion(): String {
        val sb = StringBuilder()
        sb.append(prefix)

        if (quotedValue1 != null) {
            sb.append(" „").append(quotedValue1).append("”")
        }

        sb.append(middle)

        if (quotedValue2 != null) {
            sb.append(" „").append(quotedValue2).append("”")
        }

        sb.append(suffix)

        return sb.toString()
    }
}
