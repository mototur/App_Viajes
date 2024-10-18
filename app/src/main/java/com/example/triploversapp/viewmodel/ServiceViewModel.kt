package com.example.triploversapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.triploversapp.model.Service
import com.example.triploversapp.repository.ServiceRepository

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {
    val services = mutableStateOf(listOf<Service>())

    fun loadServices() {
        services.value = repository.getServices()
    }

    fun addService(service: Service) {
        repository.addService(service)
    }
}
