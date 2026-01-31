package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.LocalQuestion

object DefaultLocalQuestions {

    val questions = listOf(

        // 1 – brak edycji pytania
        LocalQuestion(
            id = 1,
            prefix = "Kto jest Komendantem Głównym Państwowej Straży Pożarnej?",
            answers = mutableListOf(
                "nadbryg. Mariusz Feltynowski",
                "nadbryg. Wojciech Kruczek",
                "gen. brygadier Andrzej Bartkowiak"
            ),
            correctIndex = 1
        ),

        // 2 – brak edycji pytania
        LocalQuestion(
            id = 2,
            prefix = "Kto jest Prezesem Zarządu Głównego Związku OSP RP?",
            answers = mutableListOf(
                "dh Waldemar Pawlak",
                "dh Wiesław Leśniakiewicz",
                "dh Edward Siarka"
            ),
            correctIndex = 0
        ),

        // 3
        LocalQuestion(
            id = 3,
            prefix = "Kto jest",
            quotedValue1 = "małopolskim",
            middle = " Komendantem Wojewódzkim PSP?",
            answers = mutableListOf(
                "st. bryg. Marek Bębenek",
                "nadbryg. Piotr Filipek",
                "st. bryg. Przemysław Przęczek"
            ),
            correctIndex = 2
        ),

        // 5 – dwa fragmenty
        LocalQuestion(
            id = 5,
            prefix = "Kto jest Komendantem",
            quotedValue1 = "Powiatowym",
            middle = " PSP w",
            quotedValue2 = "Suchej Beskidzkiej",
            suffix = "?",
            answers = mutableListOf(
                "st. bryg. Tomasz Marek",
                "st. bryg. Krzysztof Okrzesik",
                "bryg. Łukasz Białończyk"
            ),
            correctIndex = 1
        )
        // resztę możesz dopisać analogicznie
    )
}
