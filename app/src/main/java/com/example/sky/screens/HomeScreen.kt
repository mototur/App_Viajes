package com.example.sky.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sky.model.service.Servicio
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import coil3.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen(uid: String, navController: NavHostController) {
    val services = remember { mutableStateListOf<Servicio>() }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Cargar servicios desde Firestore
    LaunchedEffect(Unit) {
        val db: FirebaseFirestore = Firebase.firestore
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
                errorMessage = "Error al cargar los servicios: ${exception.message}"
                loading = false
            }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, uid) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE3F2FD))
        ) {
            // Título de la pantalla
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
                // LazyColumn para cargar servicios
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(services.size) { index ->
                        val servicio = services[index]
                        ServiceCard(servicio)
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(servicio: Servicio) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Mostrar imagen del servicio
            Image(
                painter = rememberAsyncImagePainter(servicio.urlServicio),
                contentDescription = "Imagen del Servicio",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajusta la altura según tus necesidades
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = servicio.tipo, // O cualquier otro campo que identifique el servicio
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
                text = "Estado: ${servicio.estado}", // Asegúrate de que `estado` exista en tu modelo
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, uid: String) {
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
