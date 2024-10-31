package com.example.sky.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sky.model.message.Mensaje
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ChatScreen(uid: String, navController: NavHostController) {
    val db: FirebaseFirestore = Firebase.firestore
    val mensajes = remember { mutableStateListOf<Mensaje>() }
    var mensajeTexto by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }

    // Cargar mensajes desde Firestore
    LaunchedEffect(Unit) {
        db.collection("chat")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Manejar errores
                    return@addSnapshotListener
                }
                mensajes.clear()
                snapshot?.documents?.forEach { doc ->
                    val mensaje = doc.toObject(Mensaje::class.java)
                    if (mensaje != null) {
                        mensajes.add(mensaje)
                    }
                }
                loading = false
            }
    }

    Scaffold(
        bottomBar = { ChatNavigationBar(navController, uid) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE3F2FD))
        ) {
            // Título de la pantalla
            Text(
                text = "Chat Global",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                color = Color(0xFF1976D2)
            )

            // Lista de mensajes
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mensajes.size) { index ->
                        val mensaje = mensajes[index]
                        MessageCard(mensaje)
                    }
                }
            }

            // Campo de entrada para enviar mensaje
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = mensajeTexto,
                    onValueChange = { mensajeTexto = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (mensajeTexto.isNotBlank()) {
                        val nuevoMensaje = Mensaje(
                            autorId = uid,
                            contenido = mensajeTexto
                        )

                        db.collection("chat").add(nuevoMensaje)
                        mensajeTexto = ""
                    }
                }) {
                    Text("Enviar")
                }
            }
        }
    }
}

@Composable
fun MessageCard(mensaje: Mensaje) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = Firebase.auth.currentUser?.email ?: "Desconocido",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = mensaje.contenido,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ChatNavigationBar(navController: NavHostController, uid: String) {
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
            selected = false,
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
            selected = true,
            onClick = { navController.navigate("chat/$uid") }
        )
    }
}