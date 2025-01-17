package com.example.paltu.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if (!isValidEmail(email)) {
            callback(false, "Email not valid")
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Fetch user data
                    database.child("users").child(userId).get().addOnCompleteListener { dataTask ->
                        if (dataTask.isSuccessful) {
                            val userData = dataTask.result?.value as? Map<*, *>
                            val role = userData?.get("role") as? String
                            callback(true, role)
                        } else {
                            callback(false, "Failed to fetch user data")
                        }
                    }
                } else {
                    callback(false, "User ID not found")
                }
            } else {
                callback(false, "Incorrect login")
            }
        }
    }

    fun logout(){
        auth.signOut()
    }
fun signUp(email: String, password: String, callback: (Boolean, String?) -> Unit) {
    if (!isValidEmail(email)) {
        callback(false, "Email not valid")
        return
    }
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val userId = auth.currentUser?.uid
            if (userId != null) {

                val user = mapOf(
                    "email" to email,
                    "role" to "user"
                )
                database.child("users").child(userId).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback(true, "user")
                    } else {
                        callback(false, "Failed to store user data")
                    }
                }
            } else {
                callback(false, "User ID not found")
            }
        } else {
            callback(false, null)
        }
    }
}

fun isUserSignedIn():Boolean{
    val currentUser = auth.currentUser
    return if (currentUser != null) {
        // User is logged in, navigate to HomeActivity
        true
    } else {
        // User is not logged in, navigate to LoginActivity
        false
    }
}


private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}