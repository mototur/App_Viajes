package com.example.sky.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sky.model.user.UserAuthService
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(navController: NavHostController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val userAuthService = UserAuthService(FirebaseAuth.getInstance())

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = FontFamily.SansSerif,
            fontSize = 40.sp,
            color = Color(0xFF000000)
        )

        Spacer(Modifier.padding(24.dp))

        // Campo de texto para el email
        Column {
            Text(text = "Email", modifier = Modifier.align(Alignment.Start), fontSize = 20.sp)
            OutlinedTextField(
                value = email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    email = it
                    if (it.contains(" ")) {
                        Toast.makeText(context, "Los espacios no estan permitidos", Toast.LENGTH_SHORT).show()
                    }
                },
                label = { Text(text = "Email") }
            )
        }

        Spacer(Modifier.padding(10.dp))

        // Campo de texto para la contraseña, ocultando los caracteres
        Column {
            Text(text = "Password", modifier = Modifier.align(Alignment.Start), fontSize = 20.sp)
            OutlinedTextField(
                value = password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    password = it
                    if (it.contains(" ")) {
                        Toast.makeText(context, "Los espacios no están permitidos", Toast.LENGTH_SHORT).show()
                    }
                },
                label = { Text(text = "Contraseña") },
                visualTransformation = PasswordVisualTransformation() // Oculta los caracteres
            )
        }

        Spacer(Modifier.padding(16.dp))

        //Boton de Login
        Button(
            onClick = {
                userAuthService.loginUser(email, password) { userId ->
                    if (userId != null) {
                        Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                        navController.navigate("home/$userId")
                    } else {
                        Toast.makeText(context, "Login fallido", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF1976D2)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesion", fontSize = 20.sp)
        }

        Spacer(Modifier.padding(15.dp))

        //Boton de Registro
        Button(
            onClick = {
                navController.navigate("register")
            },
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF1976D2)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse", fontSize = 20.sp)
        }
    }
}





