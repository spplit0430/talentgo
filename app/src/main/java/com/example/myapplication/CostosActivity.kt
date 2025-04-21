package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CostosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costos)

        // Encuentra el botón de "Comenzar"
        val btnComenzar = findViewById<Button>(R.id.btn_comenzar)

        // Establecer el listener para el botón
        btnComenzar.setOnClickListener {
            // Redirigir a la primera pregunta de costos
            val intent = Intent(this, CostosPregunta1Activity::class.java)
            startActivity(intent)
        }
    }
}
