package com.example.sky.model.user

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
class UserAuthService(
    private val userAuth: FirebaseAuth
) {
    private val userFireStoreService = UserFireStoreService(FirebaseFirestore.getInstance())

    fun registerUser(usuario: Usuario, onResult: (FirebaseUser?) -> Unit) {
        userAuth.createUserWithEmailAndPassword(usuario.email, usuario.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newUser = userAuth.currentUser
                    if (newUser != null) {
                        usuario.id = newUser.uid
                        userFireStoreService.saveUser(usuario) { success, error ->
                            if (success) {
                                onResult(newUser)
                            } else {
                                println("Error al guardar en Firestore: $error")
                                onResult(null)
                            }
                        }
                    } else {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }
    }

    fun loginUser(email: String, password: String, onResult: (String?) -> Unit) {
        userAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = userAuth.currentUser
                    if (currentUser != null) {
                        onResult(currentUser.uid)
                    } else {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }
    }
}