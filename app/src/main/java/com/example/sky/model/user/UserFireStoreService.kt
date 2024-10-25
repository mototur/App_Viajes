package com.example.sky.model.user

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class UserFireStoreService(private val fireStoreInstance: FirebaseFirestore) {

    fun saveUser(usuario: Usuario, onResult: (Boolean, String?) -> Unit) {
        fireStoreInstance.collection("usuarios")
            .document(usuario.id)
            .set(usuario, SetOptions.merge())
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage)
            }
    }

    fun getUser(uid: String, callback: (Usuario?, String?) -> Unit) {
        fireStoreInstance.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    try {
                        val user = document.toObject(Usuario::class.java)
                        callback(user, null)
                    } catch (e: Exception) {
                        callback(null, "Error al convertir el documento a Usuario: ${e.message}")
                    }
                } else {
                    callback(null, "Usuario no encontrado")
                }
            }
            .addOnFailureListener { exception ->
                callback(null, exception.message)
            }
    }
}

