package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout

class activityaplicacionauniversidadesmodulo5_1 : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicacionauniversidades_modulo5_1)

        // Referencias del menú lateral
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val btnMenu = findViewById<ImageView>(R.id.btn_menu)
        val nombreLabel = findViewById<TextView>(R.id.nombre_label_2)
        val apellidoLabel = findViewById<TextView>(R.id.apellido_label_2)

        // Configurar menú lateral
        configurarMenuLateral(drawerLayout, btnMenu, nombreLabel, apellidoLabel)

        // Botón de regreso al menú principal
        val btnRegresar = findViewById<ImageView>(R.id.salida_olvido2)
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Referencias y listeners para los botones nuevos
        val btnRegistroProcesos = findViewById<Button>(R.id.btn_opcion1)
        val btnMisProcesos = findViewById<Button>(R.id.btn_opcion2)

        btnRegistroProcesos.setOnClickListener {
            val intent = Intent(this, activityaplicacionauniversidadesregistroproceso::class.java)
            startActivity(intent)
        }

        btnMisProcesos.setOnClickListener {
            val intent = Intent(this, activityaplicacionauniversidadesmisprocesos::class.java)
            startActivity(intent)
        }
    }
}
