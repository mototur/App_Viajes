package com.example.sky.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sky.model.user.UserAuthService
import com.example.sky.model.user.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelTipsScreen(uid: String, navController: NavHostController) {
    var economicTip by remember { mutableStateOf("") }
    var safetyTip by remember { mutableStateOf("") }
    var enjoymentTip by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") } // Para mostrar un mensaje de éxito
    var currentUser by remember { mutableStateOf<Usuario?>(null) }
    val userAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userAuthService: UserAuthService = UserAuthService(userAuth)

    // Cargar datos del usuario
    LaunchedEffect(uid) {
        userAuthService.getUserByUid(uid) { user ->
            if (user != null) {
                currentUser = user
            } else {
                errorMessage = "No se pudo cargar la información del usuario."
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomendaciones y Consejos de Viaje", fontSize = 25.sp, textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1976D2)),
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController, uid = uid, userName = currentUser?.nombre ?: "Usuario") }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE3F2FD)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mensajes de error y éxito
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
            }

            if (successMessage.isNotEmpty()) {
                Text(text = successMessage, color = Color.Green, modifier = Modifier.padding(16.dp))
            }

            // Títulos y campos de texto para los consejos
            Text(
                "Consejos para viajar económicamente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(10.dp) // Añadir separación
            )
            OutlinedTextField(
                value = economicTip,
                onValueChange = { economicTip = it },
                label = { Text("Añadir consejo económico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp) // Separación horizontal
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos

            Text(
                "Consejos para viajar de manera segura",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(10.dp) // Añadir separación
            )
            OutlinedTextField(
                value = safetyTip,
                onValueChange = { safetyTip = it },
                label = { Text("Añadir consejo de seguridad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp) // Separación horizontal
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos

            Text(
                "Consejos para disfrutar mejor de los destinos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(10.dp) // Añadir separación
            )
            OutlinedTextField(
                value = enjoymentTip,
                onValueChange = { enjoymentTip = it },
                label = { Text("Añadir consejo de disfrute") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp) // Separación horizontal
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón

            Button(
                onClick = {
                    if (economicTip.isNotBlank() && safetyTip.isNotBlank() && enjoymentTip.isNotBlank()) {
                        saveRecommendationToFirestore(
                            uid = uid,
                            userName = currentUser?.nombre ?: "Usuario",
                            economicTip = economicTip,
                            safetyTip = safetyTip,
                            enjoymentTip = enjoymentTip
                        ) { success, message ->
                            if (success) {
                                successMessage = message
                                // Limpiar campos
                                economicTip = ""
                                safetyTip = ""
                                enjoymentTip = ""
                                errorMessage = ""
                            } else {
                                errorMessage = message
                            }
                        }
                    } else {
                        errorMessage = "Por favor, completa todos los campos antes de enviar."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Separación horizontal
            ) {
                Text("Agregar Todos los Consejos")
            }
        }
    }
}


private fun saveRecommendationToFirestore(
    uid: String,
    userName: String,
    economicTip: String,
    safetyTip: String,
    enjoymentTip: String,
    onResult: (Boolean, String) -> Unit // Callback para manejar el resultado
) {
    val db = Firebase.firestore
    val data = mapOf(
        "userId" to uid,
        "userName" to userName,
        "economicTip" to economicTip,
        "safetyTip" to safetyTip,
        "enjoymentTip" to enjoymentTip
    )
    db.collection("recommendations").add(data)
        .addOnSuccessListener {
            onResult(true, "Recomendación agregada con éxito.")
        }
        .addOnFailureListener { exception ->
            onResult(false, "Error al agregar recomendación: ${exception.message}")
        }
}
