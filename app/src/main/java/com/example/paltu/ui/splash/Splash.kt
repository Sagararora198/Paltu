package com.example.paltu.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.paltu.R
import com.example.paltu.databinding.ActivitySplashBinding
import com.example.paltu.ui.Home.HomeScreen
import com.example.paltu.ui.auth.Login
import com.example.paltu.ui.viewmodel.LoginViewModel

class Splash : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this)
            .asGif()
            .load(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.loading)


        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.isUserSignedIn()) {
                startActivity(Intent(this, HomeScreen::class.java))
            } else {
                startActivity(Intent(this, Login::class.java))
            }
            finish() // Close the Splash Activity
        }, 4000) // 3000ms = 3 seconds
    }
}