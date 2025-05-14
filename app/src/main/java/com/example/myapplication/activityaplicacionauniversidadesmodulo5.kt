package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class activityaplicacionauniversidadesmodulo5 : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicacionauniversidades_modulo5)

        // Referencias necesarias para el menú lateral
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val btnMenu = findViewById<ImageView>(R.id.btn_menu)
        val nombreLabel = findViewById<TextView>(R.id.nombre_label_2)
        val apellidoLabel = findViewById<TextView>(R.id.apellido_label_2)

        // Configurar menú lateral con método de BaseMenuActivity
        configurarMenuLateral(drawerLayout, btnMenu, nombreLabel, apellidoLabel)

        // Botón para regresar al menú principal
        val btnRegresar = findViewById<ImageView>(R.id.salida_olvido2)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Botón para comenzar el test de costos
        val btnComenzar = findViewById<Button>(R.id.btn_comenzar)
        btnComenzar.setOnClickListener {
            val intent = Intent(this, activityaplicacionauniversidadesmodulo5_1::class.java)
            startActivity(intent)
        }
    }
}
