package com.example.triploversapp.model

data class Review(
    val userId: String = "",
    val serviceId: String = "",
    val rating: Int = 0,
    val comment: String = ""
)

