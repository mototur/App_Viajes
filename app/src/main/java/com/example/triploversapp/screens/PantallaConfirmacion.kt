package com.example.triploversapp.screens

import android.service.autofill.OnClickAction
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.triploversapp.components.DialogoConfirmacion
import com.example.triploversapp.model.Reserva

@Composable
fun PantallaConfirmacion(
    reservas: List<Reserva>,
    onAceptarClick:(Reserva) -> Unit,
    onRechazarClick:(Reserva) -> Unit
){
    LazyColumn {
        items(reservas) {reserva ->
            DialogoConfirmacion(
                reserva = reserva,
                onAceptarClick = onAceptarClick,
                onRechazarClick = onRechazarClick
            )
        }
    }
}