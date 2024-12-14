package com.example.paltu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.paltu.R
import com.example.paltu.databinding.ActivityLoginBinding
import com.example.paltu.databinding.ActivitySignUpBinding
import com.example.paltu.ui.viewmodel.LoginViewModel

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()

    }

    private fun initializeView() {
        Glide.with(this)
            .load(R.drawable.untitled_design)
            .into(binding.gifImage)

        binding.login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }
        binding.signup.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                Log.d("email",email)
                Log.d("password",password)
                viewModel.signUp(email,password){
                        success, _ ->

                    if(success){
                        Log.d("success","loggedIn")
                    }
                    else{
                        Toast.makeText(this,"Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                binding.etEmail.error = "Please fill the field"
                binding.etPassword.error = "Please fill the field"
                Toast.makeText(this,"Fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

}