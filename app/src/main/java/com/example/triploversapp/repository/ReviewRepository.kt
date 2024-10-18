package com.example.triploversapp.repository

import com.example.triploversapp.model.Review

class ReviewRepository {
    fun addReview(review: Review) {
        // Implementar lógica para agregar reseña a Firestore
    }

    fun getReviews(serviceId: String): List<Review> {
        // Implementar lógica para obtener reseñas
        return emptyList()
    }
}
