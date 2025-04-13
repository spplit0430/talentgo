package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_registrarse)

        // Botón para salir
        val salirRegistro = findViewById<ImageView>(R.id.salida_olvido2)
        salirRegistro.setOnClickListener {
            finish()
        }

        // Elementos del formulario
        val nombreText = findViewById<EditText>(R.id.nombre_text)
        val apellidoText = findViewById<EditText>(R.id.apellido_text)
        val correoText = findViewById<EditText>(R.id.correo_electronico_text)
        val contrasenaText = findViewById<EditText>(R.id.contraseña_text)
        val fechaNacimientoText = findViewById<EditText>(R.id.fecha_de_nacimiento_text)
        val registrarseBtn = findViewById<Button>(R.id.registrarse)

        registrarseBtn.setOnClickListener {
            val nombre = nombreText.text.toString().trim()
            val apellido = apellidoText.text.toString().trim()
            val correo = correoText.text.toString().trim()
            val contrasena = contrasenaText.text.toString().trim()
            val fechaNacimiento = fechaNacimientoText.text.toString().trim()

            // Validación de campos vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || fechaNacimiento.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val url = "http://192.168.1.3/usuarios_api/insertar_usuario.php"

            val request = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    try {
                        // Imprime la respuesta cruda en un Toast para verla
                        Toast.makeText(this, "Respuesta: $response", Toast.LENGTH_LONG).show()

                        val json = JSONObject(response)
                        val success = json.getBoolean("success")
                        val message = json.getString("message")

                        if (success) {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error al procesar respuesta: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Error de red: ${error.message}", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["nombre"] = nombre
                    params["apellido"] = apellido
                    params["correo_electronico"] = correo
                    params["contrasena"] = contrasena
                    params["fecha_de_nacimiento"] = fechaNacimiento
                    return params
                }
            }

            Volley.newRequestQueue(this).add(request)
        }
    }
}
