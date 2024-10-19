package com.example.triploversapp.model

data class Reserva(
    val usuario: User,
    val servicio: Service,
    val estado: String // "pendiente", "aceptado", "rechazado"
)
