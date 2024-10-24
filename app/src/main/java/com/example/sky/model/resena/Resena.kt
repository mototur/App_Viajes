package com.example.sky.model.resena

data class Reseña(
    var id: String = "",
    var autorId: String = "",
    var usuarioId: String = "",
    var servicioId: String = "",
    var calificacion: Int = 0, // 1-5
    var comentario: String = "",
    var agradecimiento: Boolean = false,
    var fecha: String = ""
)
