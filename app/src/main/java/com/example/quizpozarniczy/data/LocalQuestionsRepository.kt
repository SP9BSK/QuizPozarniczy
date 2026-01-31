package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.LocalQuestion

object LocalQuestionsRepository {

    val questions: MutableList<LocalQuestion> = mutableListOf(

        // 1 (brak edycji)
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

        // 2 (brak edycji)
        LocalQuestion(
            id = 2,
            prefix = "Kto jest Prezesem Zarządy Głównego Związku OSP RP?",
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

        // 4
        LocalQuestion(
            id = 4,
            prefix = "Kto jest Prezesem Zarządu oddziału wojewódzkiego ZOSP RP województwa",
            quotedValue1 = "małopolskiego",
            suffix = "?",
            answers = mutableListOf(
                "dh Edward Siarka",
                "dh Marek Bębenek",
                "dh Marcin Gaweł"
            ),
            correctIndex = 0
        ),

        // 5 (2 fragmenty edytowalne)
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
        ),

        // 6
        LocalQuestion(
            id = 6,
            prefix = "Kto jest Prezesem Oddziału",
            quotedValue1 = "Powiatowego",
            middle = " ZOSP RP w",
            quotedValue2 = "Suchej Beskidzkiej",
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
            quotedValue1 = "Miejsko-Gminnego",
            middle = " ZOSP RP w",
            quotedValue2 = "Makowie Podhalańskim",
            suffix = "?",
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
            prefix = "W który roku powstała Ochotnicza Straż Pożarna w",
            quotedValue1 = "Makowie Podhalańskim",
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
            quotedValue1 = "Makowie Podhalańskim",
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
            quotedValue1 = "Makowie Podhalańskim",
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
            quotedValue1 = "Makowie Podhalańskim",
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
            quotedValue1 = "Jednostki Ratowniczo Gaśniczej",
            middle = " PSP w",
            quotedValue2 = "Suchej Beskidzkiej",
            suffix = "?",
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
            prefix = "Kto jest Komendantem",
            quotedValue1 = "Gminnym",
            middle = " ZOSP RP w",
            quotedValue2 = "Makowie Podhalańskim",
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
