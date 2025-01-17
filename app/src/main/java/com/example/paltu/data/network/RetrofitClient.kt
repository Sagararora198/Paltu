package com.example.paltu.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://8790-2405-201-3001-b153-683f-ef5e-b487-4ec0.ngrok-free.app/"

    val instance: PawmbleApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PawmbleApiService::class.java)
    }
}