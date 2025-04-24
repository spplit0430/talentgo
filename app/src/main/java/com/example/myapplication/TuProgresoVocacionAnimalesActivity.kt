package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TuProgresoVocacionAnimalesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tuprogreso_vocacion_animales)

        val botonAtras = findViewById<ImageView>(R.id.atras)
        botonAtras.setOnClickListener {
            val intent = Intent(this, MetricasTuProgresoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
