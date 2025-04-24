package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AgendamientoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamiento)

        val botonAgendamiento: Button = findViewById(R.id.btn_opcion1)
        botonAgendamiento.setOnClickListener {
            val intent = Intent(this, AgendamientoAgendarActivity::class.java)
            startActivity(intent)
        }
        val btnMisCitas = findViewById<Button>(R.id.btn_opcion2)
        btnMisCitas.setOnClickListener {
            val intent = Intent(this, AgendamientoMisCitasActivity::class.java)
            startActivity(intent)
        }
        val btnCancelarCita = findViewById<Button>(R.id.btn_opcion3)
        btnCancelarCita.setOnClickListener {
            val intent = Intent(this, AgendamientoCancelarCitasActivity::class.java)
            startActivity(intent)
        }
    }
}
