package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_registrarse)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val salirRegistro = findViewById<ImageView>(R.id.salida_olvido2)
        salirRegistro.setOnClickListener {
            finish()
        }

        val nombreText = findViewById<EditText>(R.id.nombre_text)
        val apellidoText = findViewById<EditText>(R.id.apellido_text)
        val correoText = findViewById<EditText>(R.id.correo_electronico_text)
        val contrasenaText = findViewById<EditText>(R.id.contraseña_text)
        contrasenaText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        val fechaNacimientoText = findViewById<EditText>(R.id.fecha_de_nacimiento_text)
        val registrarseBtn = findViewById<Button>(R.id.registrarse)

        registrarseBtn.setOnClickListener {
            val nombre = nombreText.text.toString().trim()
            val apellido = apellidoText.text.toString().trim()
            val correo = correoText.text.toString().trim()
            val contrasena = contrasenaText.text.toString().trim()
            val fechaNacimiento = fechaNacimientoText.text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || fechaNacimiento.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar y formatear la fecha
            val formattedFechaNacimiento = formatFecha(fechaNacimiento)
            if (formattedFechaNacimiento == null) {
                Toast.makeText(this, "Fecha de nacimiento inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registro con Firebase
            auth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser


                        val profileUpdates = userProfileChangeRequest {
                            displayName = "$nombre $apellido"
                        }
                        user?.updateProfile(profileUpdates)


                        val userMap = hashMapOf(
                            "nombre" to nombre,
                            "apellido" to apellido,
                            "correo_electronico" to correo,
                            "fechaNacimiento" to fechaNacimiento
                        )

                        val userId = user?.uid

                        if (userId != null) {
                            firestore.collection("usuarios").document(userId)
                                .set(userMap)
                                .addOnSuccessListener {

                                    sendEmailVerification(user)

                                    Toast.makeText(this, "Registro exitoso, por favor verifica tu correo.", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


    private fun formatFecha(fecha: String): String? {
        val inputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        return try {
            val date = inputFormat.parse(fecha)
            outputFormat.format(date)
        } catch (e: Exception) {
            null
        }
    }


    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnSuccessListener {
                Log.d("RegisterActivity", "Correo de verificación enviado a ${user.email}")
            }
            ?.addOnFailureListener { e ->
                Log.e("RegisterActivity", "Error al enviar verificación: ${e.message}", e)
            }
    }
}
