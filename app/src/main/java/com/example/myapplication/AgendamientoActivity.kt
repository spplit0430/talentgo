package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth

class AgendamientoActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu: ImageView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamiento)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()

        // Validar sesión iniciada
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Referencias UI
        drawerLayout = findViewById(R.id.drawer_layout)
        btnMenu = findViewById(R.id.btn_menu)

        // Botón para abrir/cerrar menú lateral
        btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.openDrawer(GravityCompat.END)
            } else {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        // Botón regresar al menú principal
        val btnRegresar = findViewById<ImageView>(R.id.salida_olvido2)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Botones funcionales
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

