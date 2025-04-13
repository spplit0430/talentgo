package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        val usernameField = findViewById<EditText>(R.id.username_field)
        val passwordField = findViewById<EditText>(R.id.password_field)
        val loginButton = findViewById<Button>(R.id.login_button)

        //Enlace a Registrarse
        val registerLink = findViewById<TextView>(R.id.register_link)
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //Enlace a ¿Olvidaste tu contraseña?
        val forgotPasswordLink = findViewById<TextView>(R.id.forgot_password_link)
        forgotPasswordLink.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val correo = usernameField.text.toString().trim()
            val contrasena = passwordField.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val url = "http://192.168.1.3/usuarios_api/login_usuario.php"

            val request = object : StringRequest(
                Method.POST, url,
                { response ->
                    try {
                        println("🔍 Respuesta del servidor: $response")

                        val json = JSONObject(response)
                        val success = json.getBoolean("success")
                        val message = json.getString("message")

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                        if (success) {
                            val nombre = json.optString("nombre")
                            val apellido = json.optString("apellido")
                            val correoUsuario = json.optString("correo")
                            val fechaNacimiento = json.optString("fechaNacimiento")

                            val intent = Intent(this, MenuPrincipalActivity::class.java).apply {
                                putExtra("nombre", nombre)
                                putExtra("apellido", apellido)
                                putExtra("correo", correoUsuario)
                                putExtra("fechaNacimiento", fechaNacimiento)
                            }
                            startActivity(intent)
                            finish()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error al procesar respuesta del servidor", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Error de red: ${error.message}", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    return hashMapOf(
                        "correo_electronico" to correo,
                        "contrasena" to contrasena
                    )
                }
            }

            Volley.newRequestQueue(this).add(request)
        }
    }
}
