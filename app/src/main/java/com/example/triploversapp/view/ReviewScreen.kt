package com.example.triploversapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triploversapp.model.Review
import com.example.triploversapp.viewmodel.ReviewViewModel

@Composable
fun ReviewScreen(serviceId: String, reviewViewModel: ReviewViewModel = viewModel()) {
    val reviews by reviewViewModel.reviews

    LaunchedEffect(serviceId) {
        reviewViewModel.loadReviews(serviceId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Reseñas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reviews.size) { index ->
                val review = reviews[index]
                ReviewItem(review)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Usuario: ${review.userId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Calificación: ${review.rating} estrellas", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Comentario: ${review.comment}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddReviewScreen(serviceId: String, reviewViewModel: ReviewViewModel = viewModel()) {
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Agregar reseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Selector de calificación
        RatingSelector(rating = rating, onRatingChanged = { rating = it })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val review = Review(serviceId = serviceId, rating = rating, comment = comment)
                reviewViewModel.addReview(review)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Reseña")
        }
    }
}

@Composable
fun RatingSelector(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        repeat(5) { index ->
            IconButton(onClick = { onRatingChanged(index + 1) }) {
                Icon(
                    imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.Star,
                    contentDescription = null
                )
            }
        }
    }
}
