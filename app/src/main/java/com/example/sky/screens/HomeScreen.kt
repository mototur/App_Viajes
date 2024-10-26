package com.example.sky.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.sky.model.service.Servicio
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(uid: String, navController: NavHostController) {
    val services = remember { mutableStateListOf<Servicio>() }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    var selectedService by remember { mutableStateOf<Servicio?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    // Cargar servicios desde Firestore
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("servicios")
            .get()
            .addOnSuccessListener { documents ->
                services.clear()
                for (document in documents) {
                    val servicio = document.toObject(Servicio::class.java)
                    services.add(servicio)
                }
                loading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = "Error loading services: ${exception.message}"
                loading = false
            }
    }

    Scaffold(
        bottomBar = { BottomNavigation(navController, uid) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE3F2FD))
        ) {
            Text(
                text = "Servicios Disponibles",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                color = Color(0xFF1976D2)
            )

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(services.size) { index ->
                        val servicio = services[index]
                        ServiceCard(servicio) {
                            selectedService = servicio
                            isDialogOpen = true
                        }
                    }
                }
            }
        }
    }

    selectedService?.let { service ->
        if (isDialogOpen) {
            ServiceDetailsDialog(service) {
                isDialogOpen = false
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController, uid: String) {
    BottomNavigation(
        backgroundColor = Color(0xFF1976D2),
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = false,
            onClick = { navController.navigate("profile/$uid") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = {
                navController.navigate("home/$uid")
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Business, contentDescription = "Servicios") },
            label = { Text("Servicios") },
            selected = false,
            onClick = { navController.navigate("add_service/$uid") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
            label = { Text("Chat") },
            selected = false,
            onClick = { navController.navigate("chat/$uid") }
        )
    }
}

@Composable
fun ServiceCard(servicio: Servicio, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(servicio.urlServicio),
                contentDescription = "Imagen del Servicio",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = servicio.tipo,
                fontSize = 20.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = servicio.descripcion,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Estado: ${servicio.estado}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ServiceDetailsDialog(servicio: Servicio, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Detalles del Servicio", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(text = "Tipo: ${servicio.tipo}", fontSize = 16.sp)
                Text(text = "Descripción: ${servicio.descripcion}", fontSize = 16.sp)
                Text(text = "Ubicación: ${servicio.ubicacion}", fontSize = 16.sp)
                Text(text = "Fecha de Inicio: ${servicio.fechaInicio}", fontSize = 16.sp)
                Text(text = "Fecha de Fin: ${servicio.fechaFin}", fontSize = 16.sp)
                Text(text = "Costo: ${servicio.costo ?: "N/A"}", fontSize = 16.sp)
                Text(text = "Gratuito: ${if (servicio.gratuito) "Sí" else "No"}", fontSize = 16.sp)
                Text(text = "Estado: ${servicio.estado}", fontSize = 16.sp)
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cerrar")
            }
        }
    )
}
