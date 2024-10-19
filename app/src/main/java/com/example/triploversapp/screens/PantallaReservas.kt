package com.example.triploversapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triploversapp.components.TarjetaServicio
import com.example.triploversapp.model.Service
import com.example.triploversapp.viewmodel.ServiceViewModel

@Composable
fun PantallaReservas(
    serviceViewModel: ServiceViewModel = viewModel(), // Usamos el ViewModel para obtener los servicios
    onReservarClick: (Service) -> Unit
) {
    val servicios:List<Service> = serviceViewModel.services.value // Obtenemos los servicios desde el ViewModel

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Reservas", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el título y la lista

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(servicios) { servicio ->
                TarjetaServicio(servicio, onReservarClick)
            }
        }
    }
}

@Preview
@Composable
fun PreviewPantallaReservas() {
    PantallaReservas(onReservarClick = {})
}

