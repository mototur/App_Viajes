package com.example.sky.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.sky.model.service.Servicio
import com.example.sky.R
import com.example.sky.model.service.ServicioStorageAndStore

@Composable
fun AddServiceScreen(uid: String, navController: NavHostController, onServiceAdded: (Servicio) -> Unit) {
    var tipo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var gratuito by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    // Para la selección de imagen
    val context = LocalContext.current

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    // Instancia de ServicioStorageAndStore
    val servicioStorage = ServicioStorageAndStore()

    Scaffold(
        bottomBar = { BottomNavigation(navController, uid) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD))
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Agregar Servicio",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campos de entrada (tipo, descripción, ubicación, fechas, costo)
            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo de Servicio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = fechaInicio,
                onValueChange = { fechaInicio = it },
                label = { Text("Fecha de Inicio (dd/MM/yyyy)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = fechaFin,
                onValueChange = { fechaFin = it },
                label = { Text("Fecha de Fin (dd/MM/yyyy)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = costo,
                onValueChange = { costo = it },
                label = { Text("Costo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = gratuito,
                    onCheckedChange = { gratuito = it }
                )
                Text("Gratuito")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar Imagen")
            }

            Spacer(modifier = Modifier.height(8.dp))

            imageUri?.let {
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Seleccionada", modifier = Modifier.size(100.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
            }

            Button(
                onClick = {
                    // Validar campos y crear Servicio
                    if (tipo.isNotEmpty() && descripcion.isNotEmpty() && ubicacion.isNotEmpty() &&
                        fechaInicio.isNotEmpty() && fechaFin.isNotEmpty()) {

                        val servicio = Servicio(
                            usuarioId = uid,
                            tipo = tipo,
                            urlServicio = "", // Inicialmente vacío, se llenará después
                            descripcion = descripcion,
                            ubicacion = ubicacion,
                            fechaInicio = fechaInicio,
                            fechaFin = fechaFin,
                            costo = costo.toDoubleOrNull(),
                            gratuito = gratuito

                        )


                        // Llamar al método saveService de ServicioStorageAndStore
                        servicioStorage.saveService(servicio, imageUri) { success, errorMessage ->
                            if (success) {
                                onServiceAdded(servicio)
                                navController.popBackStack()
                            } else {
                                println("error desconocido")
                            }
                        }

                    } else {
                        errorMessage = "Por favor, completa todos los campos."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Agregar Servicio")
            }
        }
    }
}
