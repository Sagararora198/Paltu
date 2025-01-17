package com.example.paltu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.paltu.R
import com.example.paltu.databinding.ActivityLoginBinding
import com.example.paltu.ui.Home.HomeScreen
import com.example.paltu.ui.fragments.Pawmble
import com.example.paltu.ui.viewmodel.LoginViewModel

class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private val viewModel:LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeView()

        }


    private fun initializeView() {
        Glide.with(this)
            .load(R.drawable.paltu_design__4_)
            .into(binding.gifImage)
        binding.signup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        binding.login.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                    viewModel.login(email,password){
                            success, role ->
                        if(success){
                            startActivity(Intent(this,HomeScreen::class.java))
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
                Toast.makeText(this,"Fill all the fields",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
