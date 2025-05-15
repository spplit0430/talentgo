package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ContinuaVocacionalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.continua_vocacion)


        val btnContinuar: Button = findViewById(R.id.btn_continuar)


        btnContinuar.setOnClickListener {

            val intent = Intent(this, UniversidadesActivity::class.java)
            startActivity(intent)
            finish()
        }
        val botonAtras = findViewById<ImageView>(R.id.salida_general)
        botonAtras.setOnClickListener {
            val intent = Intent(this, VocacionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}