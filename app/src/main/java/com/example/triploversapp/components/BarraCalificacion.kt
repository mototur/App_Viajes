package com.example.triploversapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BarraCalificacion(
    modifier: Modifier = Modifier,
    calificacion: Int = 0,
    onCalificacionCambiada: (Int) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        for (i in 1..5) {
            IconButton(  // Usa IconButton para que el icono sea clicable
                onClick = { onCalificacionCambiada(i) }  // Maneja el click
            ) {
                Icon(
                    imageVector = if (i <= calificacion) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    tint = Color.Yellow
                )
            }
        }
    }
}
