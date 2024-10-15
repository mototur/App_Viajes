package com.example.triploversapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triploversapp.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PerfilView(viewModel: PerfilViewModel = PerfilViewModel()) {

    var user = viewModel.usuario
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Text(
                text = "Nombre: ${user.nombre}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Email: ${user.email}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Intereses: ${user.intereses}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }

}

@Preview
@Composable
fun PerfilViewPreview() {
    PerfilView()
}
