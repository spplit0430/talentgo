package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ContinuaCostosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.continua_costos)

        // Referenciar el botón
        val btnContinuar: Button = findViewById(R.id.btn_continuar)

        // Acción al presionar el botón "Continuar"
        btnContinuar.setOnClickListener {
            // Redirigir a la siguiente actividad (puedes cambiar esto según lo que necesites hacer)
            val intent = Intent(this, TuProgresoActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual si no quieres que el usuario regrese a esta pantalla al presionar atrás
        }
    }
}