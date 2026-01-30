package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.LocalQuestion

object LocalQuestionsRepository {

    val questions: MutableList<LocalQuestion> = mutableListOf(

        // 1
        LocalQuestion(
            id = 1,
            prefix = "Kto jest Komendantem Głównym Państwowej Straży Pożarnej",
            quotedValue = "",
            suffix = "?",
            answers = mutableListOf(
                "nadbryg. Wojciech Kruczek",
                "nadbryg. dr inż. Mariusz Feltynowski",
                "gen. brygadier dr inż. Andrzej Bartkowiak"
            ),
            correctIndex = 0
        ),

        // 2
        LocalQuestion(
            id = 2,
            prefix = "Kto jest Prezesem Zarządu Głównego Związku OSP RP",
            quotedValue = "",
            suffix = "?",
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
            quotedValue = "małopolskim",
            suffix = " Komendantem Wojewódzkim PSP?",
            answers = mutableListOf(
                "st. bryg. Michał Polak",
                "bryg. Marcin Głogowski",
                "st. bryg. Przemysław Przęczek"
            ),
            correctIndex = 2
        ),

        // 4
        LocalQuestion(
            id = 4,
            prefix = "Kto jest Prezesem Zarządu oddziału wojewódzkiego ZOSP RP województwa",
            quotedValue = "małopolskiego",
            suffix = "?",
            answers = mutableListOf(
                "dh Edward Siarka",
                "dh Marek Bębenek",
                "dh Marcin Gaweł"
            ),
            correctIndex = 0
        ),

        // 5
        LocalQuestion(
            id = 5,
            prefix = "Kto jest Komendantem Powiatowym PSP w",
            quotedValue = "Suchej Beskidzkiej",
            suffix = "?",
            answers = mutableListOf(
                "st. bryg. Tomasz Marek",
                "st. bryg. Krzysztof Okrzesik",
                "bryg. Łukasz Białończyk"
            ),
            correctIndex = 1
        ),

        // 6
        LocalQuestion(
            id = 6,
            prefix = "Kto jest Prezesem Oddziału Powiatowego ZOSP RP w",
            quotedValue = "Suchej Beskidzkiej",
            suffix = "?",
            answers = mutableListOf(
                "dh Andrzej Gwiazdonik",
                "dh Marcin Błachut",
                "dh Roman Wilk"
            ),
            correctIndex = 0
        ),

        // 7
        LocalQuestion(
            id = 7,
            prefix = "Kto jest Prezesem Oddziału",
            quotedValue = "Miejsko-Gminnego",
            suffix = " ZOSP RP w Makowie Podhalańskim?",
            answers = mutableListOf(
                "dh Edward Orawiec",
                "dh Stanisław Marek",
                "dh Andrzej Sala"
            ),
            correctIndex = 0
        ),

        // 8
        LocalQuestion(
            id = 8,
            prefix = "W którym roku powstała Ochotnicza Straż Pożarna w",
            quotedValue = "Makowie Podhalańskim",
            suffix = "?",
            answers = mutableListOf(
                "1962",
                "1882",
                "1924"
            ),
            correctIndex = 1
        ),

        // 9
        LocalQuestion(
            id = 9,
            prefix = "Jakie pojazdy posiada na swoim wyposażeniu Ochotnicza Straż Pożarna w",
            quotedValue = "Makowie Podhalańskim",
            suffix = "?",
            answers = mutableListOf(
                "GCBA 5/32, GBA 3/30, SLRR, SD 30",
                "GCBA 6/32, SRT, SHD 23",
                "GBA 2,5/16, GLBM 0,4/2 SLOp, SD 42"
            ),
            correctIndex = 0
        ),

        // 10
        LocalQuestion(
            id = 10,
            prefix = "Kto jest Prezesem Ochotniczej Straży Pożarnej w",
            quotedValue = "Makowie Podhalańskim",
            suffix = "?",
            answers = mutableListOf(
                "dh Piotr Fila",
                "dh Przemysław Fluder",
                "dh Maciej Musiał"
            ),
            correctIndex = 2
        ),

        // 11
        LocalQuestion(
            id = 11,
            prefix = "Kto jest Naczelnikiem Ochotniczej Straży Pożarnej w",
            quotedValue = "Makowie Podhalańskim",
            suffix = "?",
            answers = mutableListOf(
                "dh Maciej Musiał",
                "dh Mariusz Gajewski",
                "dh Przemysław Fluder"
            ),
            correctIndex = 1
        ),

        // 12
        LocalQuestion(
            id = 12,
            prefix = "Kto jest Dowódcą",
            quotedValue = "Jednostki Ratowniczo-Gaśniczej",
            suffix = " PSP w Suchej Beskidzkiej?",
            answers = mutableListOf(
                "mł. bryg. Wojciech Kania",
                "bryg. Łukasz Patera",
                "bryg. Łukasz Białończyk"
            ),
            correctIndex = 2
        ),

        // 13
        LocalQuestion(
            id = 13,
            prefix = "Kto jest Komendantem Gminnym ZOSP RP w",
            quotedValue = "Makowie Podhalańskim",
            suffix = "?",
            answers = mutableListOf(
                "dh Edward Orawiec",
                "dh Stanisław Marek",
                "dh Andrzej Sala"
            ),
            correctIndex = 1
        )
    )
}
