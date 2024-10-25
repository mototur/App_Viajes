package com.example.sky.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun BlankScreen(navController: NavController){
    LaunchedEffect(key1 = true) {
        delay(1000)
        navController.popBackStack()
        if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            navController.navigate("home/$uid")
        } else {
            navController.navigate("login")
        }
    }
    Splash()
}

@Composable
fun Splash(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun BlankScreenPreview(){
    Splash()
}
