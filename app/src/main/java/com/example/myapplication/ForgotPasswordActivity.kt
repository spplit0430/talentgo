package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_olvido)

        // Configurar el botón de salida (retorno al Login)
        val salidaOlvido = findViewById<ImageView>(R.id.salida_olvido)
        salidaOlvido.setOnClickListener {
            // Finaliza esta actividad y vuelve al Login (MainActivity)
            finish()
        }
    }
}

