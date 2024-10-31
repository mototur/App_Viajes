package com.example.sky.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                if (recommendations.isNotEmpty()) {
                    userName = recommendations[0].userName
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
                title = { Text(if (showServices) "Servicios Disponibles" else "Recomendaciones", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF000080)),
                actions = {
                    Row {
                        TextButton(onClick = { showServices = true }) {
                            Text("Servicios", color = if (showServices) Color.White else Color.White, fontSize = 17.sp)
                        }
                        TextButton(onClick = { showServices = false }) {
                            Text("Recomendaciones", color = if (!showServices) Color.White else Color.White, fontSize = 16.sp)
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController, uid, userName) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFFFF))
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

@Composable
fun BottomNavigationBar(navController: NavHostController, uid: String, userName: String? = null) {
    BottomNavigation(
        backgroundColor = Color(0xFF000080),
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("profile/$uid") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", color = Color.White) },
            selected = true,
            onClick = { navController.navigate("home/$uid") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Business, contentDescription = "Servicios") },
            label = { Text("Servicios", color = Color.White, fontSize = 13.sp) },
            selected = false,
            onClick = { navController.navigate("add_service/$uid") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
            label = { Text("Chat", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("chat/$uid") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Recomendaciones") },
            label = { Text("Consejos", color = Color.White, fontSize = 13.sp) },
            selected = false,
            onClick = { navController.navigate("travel_tips/$uid") }
        )
    }
}

@Composable
fun ServiceCard(servicio: Servicio) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isDialogOpen = true },
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
                color = Color(0xFF000080)
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

    if (isDialogOpen) {
        ServiceDetailsDialog(servicio = servicio, onDismiss = { isDialogOpen = false })
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
            TextButton(onClick = { onDismiss() }) {
                Text("Cerrar")
            }
        }
    )
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
            Text(text = "Consejo Económico: ${recommendation.economicTip}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Consejo de Seguridad: ${recommendation.safetyTip}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Consejo de Disfrute: ${recommendation.enjoymentTip}")
        }
    }
}
