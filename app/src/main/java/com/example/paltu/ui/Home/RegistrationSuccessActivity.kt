package com.example.paltu.ui.Home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paltu.R

class RegistrationSuccessActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REGISTRATION_ID = "registration_id"
        const val EXTRA_SUCCESS_MESSAGE = "success_message"

        fun start(context: Context, registrationId: String, successMessage: String) {
            val intent = Intent(context, RegistrationSuccessActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_REGISTRATION_ID, registrationId)
                putExtra(EXTRA_SUCCESS_MESSAGE, successMessage)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_success)

        val registrationId = intent.getStringExtra(EXTRA_REGISTRATION_ID) ?: ""
        val successMessage = intent.getStringExtra(EXTRA_SUCCESS_MESSAGE) ?: "Registration Successful!"

        // Set the registration ID and success message
        findViewById<TextView>(R.id.registrationId).text = registrationId
        findViewById<TextView>(R.id.successMessage).text = successMessage

        // Copy button click listener
        findViewById<Button>(R.id.copyButton).setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Registration ID", registrationId)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Registration ID copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        // Share button click listener
        findViewById<Button>(R.id.shareButton).setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "My Registration ID: $registrationId")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Registration ID"))
        }
    }
}