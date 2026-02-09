package com.example.quizpozarniczy.bluetooth

import java.util.UUID

object BtProtocol {

    const val SERVICE_NAME = "QuizMDP_BT"

    // UWAGA: NIE ZMIENIAĆ PO OPUBLIKOWANIU
    val SERVICE_UUID: UUID =
        UUID.fromString("c1a0f2b4-7c6e-4c6f-9c2a-123456789abc")

    const val BUFFER_SIZE = 1024
}
