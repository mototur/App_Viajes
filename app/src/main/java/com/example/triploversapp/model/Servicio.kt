package com.example.triploversapp.model

data class Servicio(
    val tipo: String, // Alojamiento, comida, tour, etc.
    val descripcion: String,
    val esGratuito: Boolean
)