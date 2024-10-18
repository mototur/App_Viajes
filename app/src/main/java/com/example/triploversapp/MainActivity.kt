package com.example.triploversapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triploversapp.ui.theme.TripLoversAppTheme
import com.example.triploversapp.view.AddReviewScreen
import com.example.triploversapp.view.HomeScreen
import com.example.triploversapp.view.LoginScreen
import com.example.triploversapp.view.RegisterScreen
import com.example.triploversapp.view.ReviewScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripLoversAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    AppNavigator()
                }
            }
        }
    }
}
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla de login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Navegar a HomeScreen después de un login exitoso
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                authViewModel = viewModel()
            )
        }

        // Pantalla de registro
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navegar a HomeScreen después de un registro exitoso
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                authViewModel = viewModel()
            )
        }

        // Pantalla principal Home
        composable("home") {
            HomeScreen(serviceViewModel = viewModel())
        }

        // Pantalla de reseñas para un servicio
        composable("reviews/{serviceId}") { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            serviceId?.let {
                ReviewScreen(serviceId = it, reviewViewModel = viewModel())
            }
        }

        // Pantalla para agregar una reseña
        composable("add_review/{serviceId}") { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")
            serviceId?.let {
                AddReviewScreen(serviceId = it, reviewViewModel = viewModel())
            }
        }
    }
}