package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu: ImageView

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // Datos del usuario
    private var userId: String = ""
    private var nombre = ""
    private var apellido = ""
    private var correo = ""
    private var fechaNacimiento = ""

    // ActivityResultLauncher para PerfilUsuarioActivity
    private lateinit var perfilUsuarioLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principal)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Validar sesión iniciada
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        userId = currentUser.uid

        // Referencias UI
        drawerLayout = findViewById(R.id.drawer_layout)
        btnMenu = findViewById(R.id.btn_menu)

        val textoBienvenida = findViewById<TextView>(R.id.text_bienvenida)
        val nombreLabel = findViewById<TextView>(R.id.nombre_label)
        val apellidoLabel = findViewById<TextView>(R.id.apellido_label)

        // Abrir o cerrar el menú lateral
        btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.openDrawer(GravityCompat.END)
            } else {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        // Cargar datos del usuario desde Firestore
        firestore.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    nombre = document.getString("nombre") ?: ""
                    apellido = document.getString("apellido") ?: ""
                    correo = document.getString("correo_electronico") ?: ""
                    fechaNacimiento = document.getString("fecha_de_nacimiento") ?: ""

                    // Actualizar UI
                    textoBienvenida.text = "¡Bienvenido $nombre!"
                    nombreLabel.text = nombre
                    apellidoLabel.text = apellido
                }
            }

        // Inicializar launcher para actualizar perfil
        perfilUsuarioLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Volver a cargar datos después de actualización
                firestore.collection("usuarios").document(userId).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            nombre = document.getString("nombre") ?: ""
                            apellido = document.getString("apellido") ?: ""
                            correo = document.getString("correo_electronico") ?: ""
                            fechaNacimiento = document.getString("fecha_de_nacimiento") ?: ""

                            // Actualizar UI
                            textoBienvenida.text = "¡Bienvenido $nombre!"
                            nombreLabel.text = nombre
                            apellidoLabel.text = apellido
                        }
                    }
            }
        }

        // Botón para abrir perfil
        val btnUsuario = findViewById<TextView>(R.id.btn_usuario)
        btnUsuario.setOnClickListener {
            val intent = Intent(this, PerfilUsuarioActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("apellido", apellido)
                putExtra("correo", correo)
                putExtra("fechaNacimiento", fechaNacimiento)
            }
            perfilUsuarioLauncher.launch(intent)
        }

        // Botón para cerrar sesión (salida_olvido2)
        val btnSalir1 = findViewById<ImageView>(R.id.salida_olvido2)
        btnSalir1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿Salir al inicio de sesión?")
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión y volver al login?")
            builder.setPositiveButton("Sí") { _, _ ->
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }

        // Botón para cerrar sesión (boton_derecho)
        val btnCerrarSesion2 = findViewById<ImageView>(R.id.boton_derecho)
        btnCerrarSesion2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿Cerrar sesión?")
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
            builder.setPositiveButton("Sí") { _, _ ->
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }

        // Botón para ir a la vista de Vocación (TESTS)
        val btnTests = findViewById<Button>(R.id.btn_tests)
        btnTests.setOnClickListener {
            val intent = Intent(this, VocacionActivity::class.java)
            startActivity(intent)
        }

        // Cerrar el menú lateral con el botón de salida (salida_olvido3)
        val btnCerrarMenu = findViewById<ImageView>(R.id.salida_olvido3)
        btnCerrarMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
        // Botón para ir a la vista de Universidades
        val btnConoce = findViewById<Button>(R.id.btn_conoce)
        btnConoce.setOnClickListener {
            val intent = Intent(this, UniversidadesActivity::class.java)
            startActivity(intent)
        }
        val botonPlanea = findViewById<Button>(R.id.btn_planea)
        botonPlanea.setOnClickListener {
            val intent = Intent(this, CostosActivity::class.java)
            startActivity(intent)
        }
    }
}
