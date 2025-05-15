package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth

class AgendamientoActivity : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamiento)


        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val btnMenu = findViewById<ImageView>(R.id.btn_menu)
        val nombreLabel = findViewById<TextView>(R.id.nombre_label_2)
        val apellidoLabel = findViewById<TextView>(R.id.apellido_label_2)


        configurarMenuLateral(drawerLayout, btnMenu, nombreLabel, apellidoLabel)


        val btnRegresar = findViewById<ImageView>(R.id.salida_olvido2)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

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



