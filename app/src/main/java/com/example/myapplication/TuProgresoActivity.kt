package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TuProgresoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuprogreso)

        val btnComenzar = findViewById<Button>(R.id.btn_comenzar)
        btnComenzar.setOnClickListener {
            val intent = Intent(this, MetricasTuProgresoActivity::class.java)
            startActivity(intent)
        }
    }
}
