package com.example.sky.model.message

data class Recomendacion (
    var id: String = "",
    var titulo: String = "",
    var contenido: String = "",
    var autorId: String? = null,
    var fechaPublicacion: String = "",
    var categoria: String = ""

)