package com.example.paltu.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.paltu.data.firebase.FirebaseService

class LoginViewModel :ViewModel(){
    private val authService = FirebaseService()

    fun login(email:String,password:String,callback:(Boolean,String?)->Unit){
        authService.login(email,password){success,user ->
            callback(success,user)
        }
    }
    fun signUp(email:String,password:String,callback:(Boolean,String?)->Unit){
        authService.signUp(email,password){success,user ->
            callback(success,user)
        }
    }
    fun logOut(){
        authService.logout()
    }

    fun isUserSignedIn():Boolean{
     return authService.isUserSignedIn()
    }
}