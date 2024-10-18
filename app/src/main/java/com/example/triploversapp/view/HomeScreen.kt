package com.example.triploversapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triploversapp.model.Service
import com.example.triploversapp.viewmodel.ServiceViewModel

@Composable
fun HomeScreen(serviceViewModel: ServiceViewModel = viewModel()) {
    val services by serviceViewModel.services

    LaunchedEffect(Unit) {
        serviceViewModel.loadServices()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Servicios disponibles", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(services.size) { index ->
                val service = services[index]
                ServiceItem(service)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ServiceItem(service: Service) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = service.type, style = MaterialTheme.typography.titleMedium)
            Text(text = service.description, style = MaterialTheme.typography.bodyMedium)
            if (service.isFree) {
                Text(text = "Gratis", color = MaterialTheme.colorScheme.primary)
            } else {
                Text(text = "Costo: ${service.cost} USD")
            }
        }
    }
}
