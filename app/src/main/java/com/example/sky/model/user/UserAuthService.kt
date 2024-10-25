package com.example.sky.model.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthService(
    private val userAuth: FirebaseAuth
) {
    private val userFireStoreService = UserFireStoreService(FirebaseFirestore.getInstance())

    private val firestore = FirebaseFirestore.getInstance()

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

    fun getCurrentUserUid(): String? {
        return userAuth.currentUser?.uid
    }

    fun getUser(uid: String, callback: (Usuario?, String?) -> Unit) {
        val userCollection = firestore.collection("users")
        userCollection.document(uid).get()
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


    fun getUserByUid(uid: String, callback: (Usuario?) -> Unit) {
        val userRef = firestore.collection("usuarios").document(uid)
        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val user = document.toObject(Usuario::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
    }


    fun updateUser(uid: String, updatedUser: Usuario, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(uid).set(updatedUser) // Cambiar a 'usuarios'
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }


    fun logoutUser() {
        userAuth.signOut()
    }

}

