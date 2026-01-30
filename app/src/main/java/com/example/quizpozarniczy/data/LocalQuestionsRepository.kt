package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.LocalQuestion

object LocalQuestionsRepository {

    val questions: MutableList<LocalQuestion> = mutableListOf(

        LocalQuestion(
            id = 1,
            question = "Kto jest Komendantem Głównym Państwowej Straży Pożarnej?",
            answers = mutableListOf(
                "nadbryg. Wojciech Kruczek",
                "nadbryg. dr inż. Mariusz Feltynowski",
                "gen. brygadier dr inż. Andrzej Bartkowiak"
            ),
            correctIndex = 0
        ),

        LocalQuestion(
            id = 2,
            question = "Kto jest Prezesem Zarządu Głównego Związku OSP RP?",
            answers = mutableListOf(
                "dh Waldemar Pawlak",
                "dh Wiesław Leśniakiewicz",
                "dh Edward Siarka"
            ),
            correctIndex = 0
        )
    )
}
