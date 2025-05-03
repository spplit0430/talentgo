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

abstract class BaseMenuActivity : AppCompatActivity() {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var auth: FirebaseAuth
    protected lateinit var firestore: FirebaseFirestore
    protected var userId: String = ""

    // Nuevo: launcher para esperar el resultado al regresar de PerfilUsuarioActivity
    protected lateinit var perfilUsuarioLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        userId = currentUser.uid

        // Registrar el launcher que escucha el resultado de PerfilUsuarioActivity
        perfilUsuarioLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                recargarDatosUsuario()
            }
        }
    }

    protected fun configurarMenuLateral(
        drawerLayout: DrawerLayout,
        btnMenu: ImageView,
        nombreLabel: TextView,
        apellidoLabel: TextView
    ) {
        this.drawerLayout = drawerLayout

        btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                // Cargar siempre los datos actualizados al abrir el menú
                firestore.collection("usuarios").document(userId).get()
                    .addOnSuccessListener { doc ->
                        nombreLabel.text = doc.getString("nombre") ?: ""
                        apellidoLabel.text = doc.getString("apellido") ?: ""
                    }
                drawerLayout.openDrawer(GravityCompat.END)
            } else {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        findViewById<TextView>(R.id.btn_usuario).setOnClickListener {
            val intent = Intent(this, PerfilUsuarioActivity::class.java)
            perfilUsuarioLauncher.launch(intent)
        }

        findViewById<ImageView>(R.id.boton_derecho).setOnClickListener {
            confirmarCerrarSesion()
        }

        findViewById<ImageView>(R.id.salida_olvido2).setOnClickListener {
            confirmarCerrarSesion()
        }

        findViewById<ImageView>(R.id.salida_olvido3).setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        // Navegación
        findViewById<Button>(R.id.btn_tests)?.setOnClickListener {
            startActivity(Intent(this, VocacionActivity::class.java))
        }
        findViewById<Button>(R.id.btn_conoce)?.setOnClickListener {
            startActivity(Intent(this, UniversidadesActivity::class.java))
        }
        findViewById<Button>(R.id.btn_planea)?.setOnClickListener {
            startActivity(Intent(this, CostosActivity::class.java))
        }
        findViewById<Button>(R.id.btn_visualiza)?.setOnClickListener {
            startActivity(Intent(this, TuProgresoActivity::class.java))
        }
        findViewById<TextView>(R.id.btn_agendar_cita)?.setOnClickListener {
            startActivity(Intent(this, AgendamientoActivity::class.java))
        }
        findViewById<TextView>(R.id.btn_TuProgreso)?.setOnClickListener {
            startActivity(Intent(this, TuProgresoActivity::class.java))
        }
    }

    private fun confirmarCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("¿Cerrar sesión?")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    // Nuevo: función para recargar los datos del usuario después de actualizar
    open fun recargarDatosUsuario() {
        val nombreLabel = findViewById<TextView>(R.id.nombre_label_2)
        val apellidoLabel = findViewById<TextView>(R.id.apellido_label_2)

        firestore.collection("usuarios").document(userId).get()
            .addOnSuccessListener { doc ->
                nombreLabel.text = doc.getString("nombre") ?: ""
                apellidoLabel.text = doc.getString("apellido") ?: ""
            }
    }
}
