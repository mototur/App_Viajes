package com.example.sky.model.message

data class Notificacion (
    var id: String = "",
    var usuarioId: String = "",
    var tipo: String = "", // nuevoServicio, solicitudAceptada, etc.
    var mensaje: String = "",
    var leida: Boolean = false,
    var timestamp: String = ""
)