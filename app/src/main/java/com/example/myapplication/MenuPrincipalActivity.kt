package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.FirebaseFirestore

class MenuPrincipalActivity : BaseMenuActivity() {

    private lateinit var textoBienvenida: TextView
    private lateinit var nombreLabel: TextView
    private lateinit var apellidoLabel: TextView

    private var nombre = ""
    private var apellido = ""
    private var correo = ""
    private var fechaNacimiento = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principal)

        drawerLayout = findViewById(R.id.drawer_layout)
        val btnMenu = findViewById<ImageView>(R.id.btn_menu)

        textoBienvenida = findViewById(R.id.text_bienvenida)
        nombreLabel = findViewById(R.id.nombre_label)
        apellidoLabel = findViewById(R.id.apellido_label)

        // Configura el menú con la lógica heredada
        configurarMenuLateral(drawerLayout, btnMenu, nombreLabel, apellidoLabel)

        // Cargar datos iniciales del usuario
        cargarDatosUsuario()

        // Navegación a otras secciones
        findViewById<Button>(R.id.btn_tests).setOnClickListener {
            startActivity(Intent(this, VocacionActivity::class.java))
        }

        findViewById<Button>(R.id.btn_conoce).setOnClickListener {
            startActivity(Intent(this, UniversidadesActivity::class.java))
        }

        findViewById<Button>(R.id.btn_planea).setOnClickListener {
            startActivity(Intent(this, CostosActivity::class.java))
        }

        findViewById<Button>(R.id.btn_visualiza).setOnClickListener {
            startActivity(Intent(this, TuProgresoActivity::class.java))
        }

        findViewById<TextView>(R.id.btn_agendar_cita).setOnClickListener {
            startActivity(Intent(this, AgendamientoActivity::class.java))
        }

        findViewById<TextView>(R.id.btn_TuProgreso).setOnClickListener {
            startActivity(Intent(this, TuProgresoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        firestore.collection("usuarios").document(userId)
            .get(Source.SERVER) // Fuerza obtener datos frescos del servidor
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    nombre = document.getString("nombre") ?: ""
                    apellido = document.getString("apellido") ?: ""
                    correo = document.getString("correo_electronico") ?: ""
                    fechaNacimiento = document.getString("fechaNacimiento") ?: ""

                    textoBienvenida.text = "¡Bienvenido $nombre!"
                    nombreLabel.text = nombre
                    apellidoLabel.text = apellido
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error al obtener los datos del usuario: ", exception)
            }
    }


    override fun recargarDatosUsuario() {
        cargarDatosUsuario()
    }
}
