package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UniversidadesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universidades)

        val btnComenzar = findViewById<Button>(R.id.btn_comenzar)
        btnComenzar.setOnClickListener {
            val intent = Intent(this, UniversidadesPregunta1Activity::class.java)
            startActivity(intent)
        }
    }
}
