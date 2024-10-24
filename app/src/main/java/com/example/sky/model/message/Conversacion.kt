package com.example.sky.model.message

data class Conversacion(
    var id: String = "",
    var usuario1Id: String = "",
    var usuario2Id: String = "",
    var ultimoMensaje: String = "",
    var timestamp: String= "",
    var chat: List<Mensaje> = emptyList()
)
