package com.example.triploversapp.model

data class Service(
    val id: String = "",
    val type: String = "", // hospedaje, alimentación, tour
    val description: String = "",
    val location: String = "",
    val cost: Double? = null,
    val isFree: Boolean = false
)
