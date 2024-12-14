package com.example.paltu.ui.Home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.paltu.R
import com.example.paltu.databinding.ActivityHomeScreenBinding
import com.example.paltu.ui.fragments.Pawmble

class HomeScreen : AppCompatActivity() {
    private lateinit var binding:ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Pawmble())
                .commit()
        }
        binding.navLeftButton.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, HomeFragment())
//                .commit()
        }

        binding.navCenterButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Pawmble())
                .commit()
        }

        binding.navRightButton.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, SettingsFragment())
//                .commit()
        }
    }

    }


