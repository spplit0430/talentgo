package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_olvido)


        val salidaOlvido = findViewById<ImageView>(R.id.salida_olvido)
        salidaOlvido.setOnClickListener {

            finish()
        }
    }
}

