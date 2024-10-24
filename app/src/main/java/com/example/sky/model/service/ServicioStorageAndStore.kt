package com.example.sky.model.service

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ServicioStorageAndStore {
    fun saveService(servicio: Servicio, imageUri: Uri?, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance().reference

        imageUri?.let {
            val imageRef = storage.child("services/${servicio.id}.jpg")
            imageRef.putFile(it)
                .addOnSuccessListener {
                    // Obtiene la URL
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        servicio.urlServicio = downloadUrl.toString()
                        saveToFirestore(db, servicio, onComplete)
                    }.addOnFailureListener { exception ->
                        onComplete(false, exception.message)
                    }
                }.addOnFailureListener { exception ->
                    onComplete(false, exception.message)
                }
        } ?: run {
            saveToFirestore(db, servicio, onComplete)
        }
    }

    private fun saveToFirestore(db: FirebaseFirestore, servicio: Servicio, onComplete: (Boolean, String?) -> Unit) {
        db.collection("servicios").document().set(servicio)
            .addOnSuccessListener {
                onComplete(true, null)
            }.addOnFailureListener { exception ->
                onComplete(false, exception.message)
            }
    }
}