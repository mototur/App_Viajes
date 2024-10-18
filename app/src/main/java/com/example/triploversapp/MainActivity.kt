package com.example.triploversapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.triploversapp.screens.PantallaResena
import com.example.triploversapp.ui.theme.TripLoversAppTheme
import com.example.triploversapp.view.PerfilView

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripLoversAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    // Espaciador para el contenido
                    Column(modifier = Modifier.padding(paddingValues)) {
                        // Mostrar la pantalla de perfil
                        PerfilView()

                        // Espaciador
                        Spacer(modifier = Modifier.height(16.dp))

                        // Mostrar la pantalla de reseñas
                        PantallaResena()
                    }
                }
            }
        }
    }
}



