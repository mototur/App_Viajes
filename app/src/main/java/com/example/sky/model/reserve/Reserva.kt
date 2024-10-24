package com.example.sky.model.reserve

data class Reserva(
    var id: String = "",
    var usuarioId: String = "",
    var estado: String = "pendiente", // pendiente, aceptada, rechazada
    var fechaReserva:String = ""
)