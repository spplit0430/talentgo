package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TuProgresoUniversidadesCostosDisenoModerado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tuprogreso_universidadescostos_diseno_moderado)

        val botonAtras = findViewById<ImageView>(R.id.atrass)
        botonAtras.setOnClickListener {
            val intent = Intent(this, MetricasTuProgresoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}