package com.example.paltu.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if(!isValidEmail(email)){
            callback(false,"email not valid")
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("login", "login: login successfull")
                val user = auth.currentUser
                Log.d("User info", "login: $user")
                callback(true,"success")
            } else {
                callback(false,null)
                Log.d("login failed", "login: Incorrecrt")
            }
        }
    }
    fun logout(){
        auth.signOut()
    }
    fun signUp(email: String, password: String,callback: (Boolean, String?) -> Unit){
        if(!isValidEmail(email)){
            callback(false,"email not valid")
        }
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Log.d("signup","signUp success")
                callback(true,"user")
            }
            else{
                callback(false,null)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}