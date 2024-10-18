package com.example.triploversapp.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(
    val id: String = "",
    val name: String = "",
    val interests: String = "",
    val travelExperience: String = "",
    val servicesOffered: List<Service> = listOf(),
    val email: String = ""
)

