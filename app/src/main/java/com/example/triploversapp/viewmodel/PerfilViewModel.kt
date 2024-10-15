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
        Servicio(
            tipo = "Alojamiento",
            descripcion = "Habitación en casa familiar",
            esGratuito = true,
            nombre = "Casa de John",
            costo = 0.0
        ),
        Servicio(
            tipo = "Comida",
            descripcion = "Desayuno típico local",
            esGratuito = true,
            nombre = "Desayuno de John",
            costo = 0.0
        ),
        Servicio(
            tipo = "Tour",
            descripcion = "Paseo por la ciudad",
            esGratuito = true,
            nombre = "Tour de la ciudad",
            costo = 0.0
        )
    )
    // Método para obtener los servicios
    fun obtenerServicios(): List<Servicio> {
        return servicios
    }
}
