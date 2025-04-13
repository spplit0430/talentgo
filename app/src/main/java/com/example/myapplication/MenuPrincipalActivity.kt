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

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu: ImageView

    // Variables para almacenar los datos del usuario
    private var nombre = ""
    private var apellido = ""
    private var correo = ""
    private var fechaNacimiento = ""

    // ActivityResultLauncher para PerfilUsuarioActivity
    private lateinit var perfilUsuarioLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principal)

        // Inicializar el DrawerLayout y el botón del menú
        drawerLayout = findViewById(R.id.drawer_layout)
        btnMenu = findViewById(R.id.btn_menu)

        btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.openDrawer(GravityCompat.END)
            } else {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        // Recibir los datos enviados desde LoginActivity o PerfilUsuarioActivity
        nombre = intent.getStringExtra("nombre") ?: ""
        apellido = intent.getStringExtra("apellido") ?: ""
        correo = intent.getStringExtra("correo") ?: ""
        fechaNacimiento = intent.getStringExtra("fechaNacimiento") ?: ""

        // Mostrar el nombre en el TextView de bienvenida
        val textoBienvenida = findViewById<TextView>(R.id.text_bienvenida)
        textoBienvenida.text = "¡Bienvenido $nombre!"

        // Inicializar el launcher para recibir resultados de PerfilUsuarioActivity
        perfilUsuarioLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                // Actualizar datos con los que vienen desde PerfilUsuarioActivity
                val data = result.data!!
                nombre = data.getStringExtra("nombre") ?: ""
                apellido = data.getStringExtra("apellido") ?: ""
                correo = data.getStringExtra("correo") ?: ""
                fechaNacimiento = data.getStringExtra("fechaNacimiento") ?: ""

                // Actualizar texto de bienvenida
                textoBienvenida.text = "¡Bienvenido $nombre!"
            }
        }

        // Botón USUARIO para ir a PerfilUsuarioActivity
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

        // Botón para salir de sesión
        val btnSalir = findViewById<ImageView>(R.id.salida_olvido2)
        btnSalir.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿Salir al inicio de sesión?")
            builder.setMessage("¿Estás seguro de que deseas cerrar sesión y volver al login?")
            builder.setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }
}
