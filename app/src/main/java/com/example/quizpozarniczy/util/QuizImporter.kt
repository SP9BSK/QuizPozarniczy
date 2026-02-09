package com.example.quizpozarniczy.util

import android.content.Context
import com.example.quizpozarniczy.model.LocalQuestion
import com.example.quizpozarniczy.model.Question
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader

object QuizImporter {

    private val gson = Gson()

    data class ImportQuestion(
        val type: String,
        val id: Int? = null,
        val text: String? = null,
        val prefix: String? = null,
        val quotedValue1: String? = null,
        val middle: String? = null,
        val quotedValue2: String? = null,
        val suffix: String? = null,
        val answers: List<String>,
        val correctIndex: Int
    )

    data class ImportPackage(
        val questions: List<ImportQuestion>
    )

    /**
     * =========================
     * Wczytuje JSON z InputStream (plik)
     * =========================
     */
    fun importQuiz(context: Context, inputStream: InputStream): Pair<List<Question>, List<LocalQuestion>> {
        val reader = InputStreamReader(inputStream)
        val importPackage = gson.fromJson(reader, ImportPackage::class.java)
        return parseImportPackage(importPackage)
    }

    /**
     * =========================
     * Nowe – import z JSON-a w formie String (QR)
     * =========================
     */
    fun importQuizFromString(json: String): Pair<List<Question>, List<LocalQuestion>> {
        return try {
            val importPackage = gson.fromJson(json, ImportPackage::class.java)
            parseImportPackage(importPackage)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(emptyList(), emptyList())
        }
    }

    /**
     * =========================
     * Wspólna logika parsowania
     * =========================
     */
    private fun parseImportPackage(importPackage: ImportPackage): Pair<List<Question>, List<LocalQuestion>> {
        val generalQuestions = mutableListOf<Question>()
        val localQuestions = mutableListOf<LocalQuestion>()

        importPackage.questions.forEach { q ->
            when (q.type) {
                "general" -> {
                    if (q.text != null) {
                        generalQuestions.add(
                            Question(
                                text = q.text,
                                answers = q.answers,
                                correctIndex = q.correctIndex
                            )
                        )
                    }
                }
                "local" -> {
                    localQuestions.add(
                        LocalQuestion(
                            id = q.id ?: 0,
                            prefix = q.prefix ?: "",
                            quotedValue1 = q.quotedValue1,
                            middle = q.middle ?: "",
                            quotedValue2 = q.quotedValue2,
                            suffix = q.suffix ?: "",
                            answers = q.answers.toMutableList(),
                            correctIndex = q.correctIndex
                        )
                    )
                }
            }
        }

        return Pair(generalQuestions, localQuestions)
    }
}
