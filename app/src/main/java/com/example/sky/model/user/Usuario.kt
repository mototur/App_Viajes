package com.example.sky.model.user

import com.example.sky.model.resena.Reseña
import com.example.sky.model.service.Servicio

data class Usuario(
    var id: String = "",
    var nombre: String = "",
    var email: String = "",
    var password: String ="",
    var intereses: List<String> = emptyList(),
    var experienciaViaje: String = "",
    var serviciosOfrecidos: List<Servicio> = emptyList(),
    var reseñasRecibidas: List<Reseña> = emptyList()
)

