package com.example.sky.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.sky.model.recomendation.Recomendation
import com.example.sky.model.service.Servicio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(uid: String, navController: NavHostController) {
    val services = remember { mutableStateListOf<Servicio>() }
    val recommendations = remember { mutableStateListOf<Recomendation>() }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showServices by remember { mutableStateOf(true) }
    var userName by remember { mutableStateOf("Usuario") }
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            userName = currentUser.displayName ?: "Usuario"
        }
    }

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

    LaunchedEffect(Unit) {
        val db: FirebaseFirestore = Firebase.firestore
        db.collection("recommendations")
            .get()
            .addOnSuccessListener { documents ->
                recommendations.clear()
                for (document in documents) {
                    val recommendation = Recomendation(
                        userName = document.getString("userName") ?: "Usuario",
                        economicTip = document.getString("economicTip") ?: "",
                        safetyTip = document.getString("safetyTip") ?: "",
                        enjoymentTip = document.getString("enjoymentTip") ?: ""
                    )
                    recommendations.add(recommendation)
                }
                // Actualizar userName al cargar las recomendaciones
                if (recommendations.isNotEmpty()) {
                    userName = recommendations[0].userName // Asigna el userName del primer elemento
                }
                loading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = "Error al cargar las recomendaciones: ${exception.message}"
                loading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (showServices) "Servicios Disponibles" else "Recomendaciones") },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1976D2)),
                actions = {
                    Row {
                        TextButton(onClick = { showServices = true }) {
                            Text("Servicios", color = if (showServices) Color.White else Color.Gray)
                        }
                        TextButton(onClick = { showServices = false }) {
                            Text("Recomendaciones", color = if (!showServices) Color.White else Color.Gray)
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController, uid, userName) } // Pasa el userName aquí
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE3F2FD))
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                if (showServices) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(services.size) { index ->
                            val servicio = services[index]
                            ServiceCard(servicio)
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(recommendations.size) { index ->
                                val recommendation = recommendations[index]
                                RecommendationCard(recommendation)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, uid: String, userName: String? = null) {
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
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Recomendaciones") },
            label = { Text("Consejos") },
            selected = false,
            onClick = { navController.navigate("travel_tips/$uid") }
        )
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
fun RecommendationCard(recommendation: Recomendation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recommendation.userName, fontSize = 18.sp, color = Color(0xFF1976D2))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Consejo económico: ${recommendation.economicTip}", fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Consejo de seguridad: ${recommendation.safetyTip}", fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Consejo para disfrutar: ${recommendation.enjoymentTip}", fontSize = 14.sp, color = Color.Black)
        }
    }
}
