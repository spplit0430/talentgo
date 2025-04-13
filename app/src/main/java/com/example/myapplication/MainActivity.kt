package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Decisión para cargar el layout apropiado
        val isLogin = intent.getBooleanExtra("IS_LOGIN", true) // Define si es login o registrar

        if (isLogin) {
            setContentView(R.layout.activity_main_login) // Layout de login

            // Configurar el enlace "Registrarse"
            val registerLink = findViewById<TextView>(R.id.register_link)
            registerLink.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java) // Actividad de registro
                startActivity(intent)
            }

            val forgotPasswordLink = findViewById<TextView>(R.id.forgot_password_link)
            forgotPasswordLink.setOnClickListener {
                val intent = Intent(this, ForgotPasswordActivity::class.java) // Actividad de olvido de contraseña
                startActivity(intent)
            }

        } else {
            setContentView(R.layout.activity_main_registrarse) // Layout de registro
        }

        // Ajustes comunes para ambos layouts
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}