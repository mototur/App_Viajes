package com.example.triploversapp.model

data class Reserva(
    val usuario: Usuario,
    val servicio: Servicio,
    val estado: String // "pendiente", "aceptado", "rechazado"
)
