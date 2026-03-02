package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.Question
import com.example.quizpozarniczy.R

val questionsPart22 = listOf(

    Question(
        id = 505,
        text = "Na obrazku przedstawiono stopień:",
        answers = listOf(
            "strażak",
            "starszy strażak",
            "sekcyjny"
        ),
        correctIndex = 0,
        imageResId = R.drawable.strazak
    ),

    Question(
        id = 506,
        text = "Na obrazku przedstawiono stopień:",
        answers = listOf(
            "strażak",
            "starszy strażak",
            "młodszy ogniomistrz"
        ),
        correctIndex = 1,
        imageResId = R.drawable.starszy_strazak
    ),

    Question(
        id = 507,
        text = "Na obrazku przedstawiono stopień:",
        answers = listOf(
            "sekcyjny",
            "starszy sekcyjny",
            "strażak"
        ),
        correctIndex = 0,
        imageResId = R.drawable.sekcyjny
    ),

    Question(
        id = 508,
        text = "Na obrazku przedstawiono stopień:",
        answers = listOf(
            "sekcyjny",
            "starszy sekcyjny",
            "młodszy ogniomistrz"
        ),
        correctIndex = 1,
        imageResId = R.drawable.starszy_sekcyjny
    ),

    Question(
        id = 509,
        text = "Na obrazku przedstawiono stopień:",
        answers = listOf(
            "młodszy ogniomistrz",
            "sekcyjny",
            "starszy strażak"
        ),
        correctIndex = 0,
        imageResId = R.drawable.mlodszy_ogniomistrz
    )
)
