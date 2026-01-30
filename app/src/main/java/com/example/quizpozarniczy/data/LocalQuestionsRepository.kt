package com.example.quizpozarniczy.data

import com.example.quizpozarniczy.model.LocalQuestion

object LocalQuestionsRepository {

    val questions: MutableList<LocalQuestion> = mutableListOf(

        // 1
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

        // 2
        LocalQuestion(
            id = 2,
            question = "Kto jest Prezesem Zarządu Głównego Związku OSP RP?",
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
            question = "Kto jest „małopolskim” Komendantem Wojewódzkim PSP?",
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
            question = "Kto jest Prezesem Zarządu oddziału wojewódzkiego ZOSP RP województwa „małopolskiego”?",
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
            question = "Kto jest Komendantem Powiatowym PSP w „Suchej Beskidzkiej”?",
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
            question = "Kto jest Prezesem Oddziału Powiatowego ZOSP RP w „Suchej Beskidzkiej”?",
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
            question = "Kto jest Prezesem Oddziału „Miejsko-Gminnego” ZOSP RP w „Makowie Podhalańskim”?",
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
            question = "W którym roku powstała Ochotnicza Straż Pożarna w „Makowie Podhalańskim”?",
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
            question = "Jakie pojazdy posiada na swoim wyposażeniu Ochotnicza Straż Pożarna w „Makowie Podhalańskim”?",
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
            question = "Kto jest Prezesem Ochotniczej Straży Pożarnej w „Makowie Podhalańskim”?",
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
            question = "Kto jest Naczelnikiem Ochotniczej Straży Pożarnej w „Makowie Podhalańskim”?",
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
            question = "Kto jest Dowódcą „Jednostki Ratowniczo-Gaśniczej” PSP w „Suchej Beskidzkiej”?",
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
            question = "Kto jest Komendantem Gminnym ZOSP RP w „Makowie Podhalańskim”?",
            answers = mutableListOf(
                "dh Edward Orawiec",
                "dh Stanisław Marek",
                "dh Andrzej Sala"
            ),
            correctIndex = 1
        )
    )
}
