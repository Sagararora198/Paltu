package com.example.paltu.data.model

data class PawmbleCardDetail(
    val tag_id: Int,
    val age: Int,
    val type: String,
    val gender: String,
    val fitness: String,
    val sterilisation: Boolean,
    val vaccination: Boolean,
    val caretaker:String,
    val contact: String,
    val photos: String,
    val available: Boolean

)