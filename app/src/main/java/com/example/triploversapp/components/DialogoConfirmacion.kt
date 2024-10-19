package com.example.triploversapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.triploversapp.model.Reserva

@Composable
fun DialogoConfirmacion(
    reserva: Reserva,
    onAceptarClick: (Reserva) -> Unit,
    onRechazarClick: (Reserva) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Solicitud de reserva de: ${reserva.usuario.name}")
            Text(text = "Servicio: ${reserva.servicio.id}")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button (onClick = { onAceptarClick(reserva) }, modifier = Modifier.weight(1f)) {
                    Text("Aceptar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onRechazarClick(reserva) }, modifier = Modifier.weight(1f)) {
                    Text("Rechazar")
                }
            }
        }
    }
}