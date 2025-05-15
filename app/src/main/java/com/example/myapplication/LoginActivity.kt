package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        auth = FirebaseAuth.getInstance()

        val usernameField = findViewById<EditText>(R.id.username_field)
        val passwordField = findViewById<EditText>(R.id.password_field)
        val loginButton = findViewById<Button>(R.id.login_button)


        val registerLink = findViewById<TextView>(R.id.register_link)
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


        val forgotPasswordLink = findViewById<TextView>(R.id.forgot_password_link)
        forgotPasswordLink.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }


        loginButton.setOnClickListener {
            val correo = usernameField.text.toString().trim()
            val contrasena = passwordField.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {

                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MenuPrincipalActivity::class.java).apply {
                                putExtra("correo", user.email)
                            }
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(this, "Por favor, verifica tu correo antes de iniciar sesión.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
