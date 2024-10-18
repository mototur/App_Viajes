package com.example.triploversapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.triploversapp.components.BarraCalificacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaResena() {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Califica tu experiencia") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF6200EE),  // Color de fondo
                    titleContentColor = Color.White  // Color del título
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Texto descriptivo
            Text(
                text = "Por favor, califica tu experiencia con el servicio:",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Sección de calificación (estrellas)
            BarraCalificacion(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                calificacion = 0 // Valor inicial
            )

            // Campo de texto para la reseña
            OutlinedTextField(
                value = "", // Aquí manejarás el estado del texto
                onValueChange = { /* Manejar el texto ingresado */ },
                label = { Text("Escribe tu reseña") },
                placeholder = { Text("Comparte tu experiencia...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            // Espaciador
            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Acción para enviar la calificación */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Enviar")
                }

                Spacer(modifier = Modifier.width(16.dp))

                OutlinedButton(
                    onClick = { /* Acción para cancelar */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
