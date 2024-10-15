package com.example.triploversapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triploversapp.model.Servicio

@Composable
fun TarjetaServicio(servicio: Servicio, onReservarClick: (Servicio) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = servicio.nombre, style = MaterialTheme.typography.titleLarge)
            Text(text = servicio.descripcion)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onReservarClick(servicio) }) {
                Text("Reservar")
            }
        }
    }
}