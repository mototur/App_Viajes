package com.example.triploversapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triploversapp.model.Review
import com.example.triploversapp.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel(private val reviewRepository: ReviewRepository) : ViewModel() {
    // Estado mutable que contiene las reseñas
    val reviews = mutableStateOf<List<Review>>(listOf())

    // Función para cargar reseñas basadas en el ID del servicio
    fun loadReviews(serviceId: String) {
        viewModelScope.launch {
            val fetchedReviews = reviewRepository.getReviews(serviceId)
            reviews.value = fetchedReviews
        }
    }

    // Función para agregar una nueva reseña
    fun addReview(review: Review) {
        viewModelScope.launch {
            reviewRepository.addReview(review)
            // Recargar las reseñas después de agregar una nueva
            loadReviews(review.serviceId)
        }
    }
}
