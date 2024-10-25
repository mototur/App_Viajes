package com.example.sky.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.sky.model.user.UserAuthService
import com.example.sky.model.user.Usuario

@Composable
fun ProfileScreen(navController: NavHostController, userAuthService: UserAuthService, uid: String) {
    var currentUser by remember { mutableStateOf<Usuario?>(null) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    // Cargar datos del usuario
    LaunchedEffect(uid) {
        userAuthService.getUserByUid(uid) { user ->
            if (user != null) {
                currentUser = user
            } else {
                errorMessage = "No se pudo cargar la información del usuario."
            }
            loading = false
        }
    }

    // Asegúrate de que currentUser no sea nulo
    if (currentUser == null) {
        Text(text = "No se encontró información del usuario.", color = Color.Red)
        return
    }

    var nombre by remember { mutableStateOf(TextFieldValue(currentUser!!.nombre)) }
    var email by remember { mutableStateOf(TextFieldValue(currentUser!!.email)) }
    var password by remember { mutableStateOf(TextFieldValue(currentUser!!.password)) }
    var intereses by remember { mutableStateOf(TextFieldValue(currentUser!!.intereses.joinToString(", "))) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil de Usuario",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = intereses,
            onValueChange = { intereses = it },
            label = { Text("Intereses (separados por comas)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEditing
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (successMessage.isNotEmpty()) {
            Text(text = successMessage, color = Color.Green, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isEditing) {
            Button(
                onClick = {
                    if (nombre.text.isNotEmpty() && email.text.isNotEmpty()) {
                        val updatedUser = Usuario(
                            nombre = nombre.text,
                            email = email.text,
                            intereses = intereses.text.split(",").map { it.trim() }
                        )

                        userAuthService.updateUser(uid, updatedUser) { isSuccess ->
                            if (isSuccess) {
                                successMessage = "Información actualizada con éxito."
                                errorMessage = ""
                                isEditing = false
                            } else {
                                errorMessage = "Error al actualizar la información."
                                successMessage = ""
                            }
                        }
                    } else {
                        errorMessage = "Por favor, complete todos los campos"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Guardar", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = { isEditing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Editar", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = {
                userAuthService.logoutUser()
                navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Cerrar Sesión", fontSize = 18.sp)
        }
    }
}
