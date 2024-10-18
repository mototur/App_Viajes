package com.example.triploversapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.triploversapp.model.User
import com.example.triploversapp.repository.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String) {
        repository.login(email, password)
    }

    fun register(user: User) {
        repository.register(user)
    }
}
