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
}