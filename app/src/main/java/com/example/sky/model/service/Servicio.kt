package com.example.sky.model.service

import com.example.sky.model.reserve.Reserva

data class Servicio(
    var id: String = "",
    var usuarioId: String = "",
    var tipo: String = "", // hospedaje, alimentación, tour
    var descripcion: String = "",
    var urlServicio: String = "",
    var ubicacion:String  = "",
    var fechaInicio:String  = "",
    var fechaFin: String ="",
    var costo: Double? = null,
    var gratuito: Boolean = false,
    var estado: String = "disponible", // disponible, reservado, no disponible
    var reservas: List<Reserva> = emptyList()

)

