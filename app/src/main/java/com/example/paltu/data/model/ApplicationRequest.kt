package com.example.paltu.data.model

import android.net.Uri
import java.io.File

data class ApplicationRequest(
    val tag_id: Int,
    val name: String,
    val contact: String,
    val whatsapp: String,
    val address: String,
    val occupation: String,
    val email: String,
    val adopterImage:File,
    val adopterDoc: File,
    val incamp: String
)


