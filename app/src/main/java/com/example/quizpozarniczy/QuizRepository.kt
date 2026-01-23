package com.example.quizpozarniczy

import com.example.quizpozarniczy.model.Question

object QuizRepository {

    fun getQuestions(): List<Question> {
        return listOf(

            Question("Jaki jest numer alarmowy straży pożarnej?", listOf("997","998","112","999"), 1),
            Question("Co oznacza skrót OSP?", listOf("Ochotnicza Straż Pożarna","Oficjalna Służba Pożarna","Obrona Specjalna Pożarowa","Organizacja Strażacka Państwowa"), 0),
            Question("Jakiego koloru jest gaśnica proszkowa?", listOf("Zielona","Niebieska","Czerwona","Żółta"), 2),
            Question("Którego środka nie wolno używać do gaszenia prądu?", listOf("Proszek","CO₂","Piasek","Woda"), 3),
            Question("Pożary cieczy palnych oznacza się literą:", listOf("A","B","C","D"), 1),
            Question("Minimalny skład zastępu OSP to:", listOf("2","3","4","6"), 1),
            Question("Jakie napięcie uznaje się za niebezpieczne?", listOf("12 V","24 V","50 V","110 V"), 2),
            Question("Dowódca zastępu to:", listOf("DS","KDR","Prezes","Naczelnik"), 0),
            Question("Co oznacza skrót KSRG?", listOf("Krajowy System Ratowniczo-Gaśniczy","Korpus Straży Ratowniczej","Krajowa Służba Ratownicza","Komenda Straży"), 0),
            Question("Podstawowym zadaniem straży jest:", listOf("Gaszenie pożarów","Ratowanie życia","Usuwanie klęsk","Wszystkie poprawne"), 3),

            Question("Gaśnica CO₂ jest przeznaczona głównie do:", listOf("Drewna","Cieczy","Urządzeń elektrycznych","Metali"), 2),
            Question("Hełm strażacki chroni:", listOf("Tylko głowę","Głowę i kark","Tylko twarz","Całe ciało"), 1),
            Question("Linia gaśnicza składa się z:", listOf("Prądownicy","Węży","Pompy","Rozdzielacza"), 1),
            Question("Rozdzielacz służy do:", listOf("Zwiększania ciśnienia","Rozdziału wody","Zasysania wody","Gaszenia"), 1),
            Question("Prądownica pianowa wytwarza:", listOf("Mgłę","Pianę","Proszek","CO₂"), 1),
            Question("Piana gaśnicza odcina:", listOf("Ciepło","Tlen","Wodę","Dym"), 1),
            Question("Pożary metali oznacza się literą:", listOf("A","B","C","D"), 3),
            Question("Gaśnica śniegowa zawiera:", listOf("Proszek","Azot","CO₂","Pianę"), 2),
            Question("Pierwszym krokiem RKO jest:", listOf("Oddechy","Ucisk klatki","Sprawdzenie przytomności","Wezwanie pomocy"), 2),
            Question("Numer alarmowy UE to:", listOf("998","999","997","112"), 3),

            Question("PSP to:", listOf("Państwowa Straż Pożarna","Powiatowa Straż","Publiczna Straż","Polska Straż"), 0),
            Question("Dowodzenie akcją prowadzi:", listOf("Ratownik","Naczelnik","KDR","Prezes"), 2),
            Question("Autopompa służy do:", listOf("Gaszenia","Tłoczenia wody","Zasysania powietrza","Wytwarzania piany"), 1),
            Question("Hydrant wewnętrzny jest koloru:", listOf("Niebieskiego","Czerwonego","Zielonego","Żółtego"), 1),
            Question("Zadaniem KDR jest:", listOf("Gaszenie","Dowodzenie","Ewakuacja","Zabezpieczenie"), 1),
            Question("Drabina strażacka służy do:", listOf("Gaszenia","Ewakuacji","Zasilania","Chłodzenia"), 1),
            Question("Podczas pożaru należy poruszać się:", listOf("Wyprostowany","Przy podłodze","Biegiem","Bez znaczenia"), 1),
            Question("Sprzęt ochrony dróg oddechowych to:", listOf("Hełm","Maska","Buty","Rękawice"), 1),
            Question("Zadaniem OSP jest:", listOf("Tylko pożary","Ratownictwo","Pomoc","Wszystkie"), 3),
            Question("Motopompa to:", listOf("Pompa przenośna","Pompa ręczna","Pompa elektryczna","Gaśnica"), 0),

            Question("Gaśnica proszkowa gasi pożary:", listOf("A","B","C","Wszystkie"), 3),
            Question("Czas reakcji RKO to:", listOf("Do 1 min","Do 3 min","Do 5 min","Bez znaczenia"), 1),
            Question("Środki ochrony indywidualnej to:", listOf("Hełm","Rękawice","Buty","Wszystkie"), 3),
            Question("Pożar klasy A to:", listOf("Metale","Ciecze","Gazy","Ciała stałe"), 3),
            Question("Pożar klasy C to:", listOf("Ciecze","Gazy","Metale","Drewno"), 1),
            Question("Zasysanie wody odbywa się przez:", listOf("Wąż tłoczny","Wąż ssawny","Rozdzielacz","Prądownicę"), 1),
            Question("Najczęstsza przyczyna pożarów to:", listOf("Podpalenia","Zwarcia","Nieostrożność","Wyładowania"), 2),
            Question("Podstawowy środek gaśniczy to:", listOf("Woda","Piana","Proszek","CO₂"), 0),
            Question("Podczas ewakuacji nie wolno:", listOf("Pomagać","Biec","Używać windy","Wychodzić"), 2),
            Question("Maska ODO chroni:", listOf("Słuch","Wzrok","Drogi oddechowe","Głowę"), 2),

            Question("Gaśnica powinna wisieć:", listOf("Na podłodze","Na ścianie","W szafce","W aucie"), 1),
            Question("Czujnik dymu reaguje na:", listOf("Ciepło","Ogień","Dym","Gaz"), 2),
            Question("Wąż W52 ma średnicę:", listOf("25 mm","52 mm","75 mm","110 mm"), 1),
            Question("Wąż W75 służy do:", listOf("Zasilania","Gaszenia","Ewakuacji","Chłodzenia"), 0),
            Question("Sprzęt burzący to:", listOf("Siekiera","Gaśnica","Pompa","Wąż"), 0),
            Question("Kask strażacki jest obowiązkowy:", listOf("Zawsze","Na ćwiczeniach","Na akcji","Zawsze"), 3),
            Question("Zastęp to:", listOf("1 strażak","2 strażaków","Grupa strażaków","Jednostka"), 2),
            Question("Rota składa się z:", listOf("1 osoby","2 osób","3 osób","4 osób"), 1),
            Question("Zadaniem roty jest:", listOf("Dowodzenie","Gaszenie","Ratowanie","Wszystkie"), 2),
            Question("Latarka strażacka powinna być:", listOf("Zwykła","Iskrobezpieczna","Duża","Mała"), 1)
        )
    }
}
