package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ContinuaUniversidadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.continua_universidades)

        // Referenciar el botón
        val btnContinuar: Button = findViewById(R.id.btn_continuar)

        // Acción al presionar el botón "Continuar"
        btnContinuar.setOnClickListener {
            // Redirigir a la siguiente actividad (puedes cambiar esto según lo que necesites hacer)
            val intent = Intent(this, CostosActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual si no quieres que el usuario regrese a esta pantalla al presionar atrás
        }
        val botonAtras = findViewById<ImageView>(R.id.salida_general)
        botonAtras.setOnClickListener {
            val intent = Intent(this, UniversidadesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}