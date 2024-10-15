package com.example.triploversapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.triploversapp.model.Servicio
import com.example.triploversapp.model.Usuario

class PerfilViewModel : ViewModel() {

    val usuario = Usuario(
        nombre = "John Doe",
        email = "johndoe@example.com",
        intereses = "Viajar, Fotografía"
    )

    val servicios = listOf(
        Servicio(tipo = "Alojamiento", descripcion = "Habitación en casa familiar", esGratuito = false),
        Servicio(tipo = "Comida", descripcion = "Desayuno típico local", esGratuito = true),
        Servicio(tipo = "Tour", descripcion = "Paseo por la ciudad", esGratuito = true)
    )
}