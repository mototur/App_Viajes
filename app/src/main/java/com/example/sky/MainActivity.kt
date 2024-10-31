package com.example.sky

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sky.model.user.UserAuthService
import com.example.sky.screens.AddServiceScreen
import com.example.sky.screens.BlankScreen
import com.example.sky.screens.HomeScreen
import com.example.sky.screens.Login
import com.example.sky.screens.ProfileScreen
import com.example.sky.screens.RegisterScreen
import com.example.sky.screens.TravelTipsScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val firebaseAuth: UserAuthService = UserAuthService(FirebaseAuth.getInstance())

    NavHost(navController = navController, startDestination = "blank") {
        composable("home/{userID}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("userID")
            if (uid != null) {
                HomeScreen(uid, navController)
            } else {
                Toast.makeText(navController.context, "Error al obtener el UID", Toast.LENGTH_SHORT).show()
            }
        }

        composable("blank") {
            BlankScreen(navController)
        }
        composable("travel_tips/{uid}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: return@composable
            TravelTipsScreen(uid, navController)
        }
        composable("profile/{userID}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userID")
            if (userId != null) {
                ProfileScreen(navController, firebaseAuth, userId)
            } else {
                Toast.makeText(navController.context, "No se proporcionó el UID", Toast.LENGTH_SHORT).show()
            }
        }

        composable("login") { Login(navController) }
        composable("register") { RegisterScreen(navController, firebaseAuth) }
        composable("add_service/{userID}") { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")
            if (userID != null) {
                AddServiceScreen(uid = userID, navController = navController, onServiceAdded = { servicio ->
                    navController.navigate("home/$userID")
                })
            }
        }
    }
}