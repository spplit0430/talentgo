package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActualizarUsuarioActivity : AppCompatActivity() {

    private lateinit var nombreText: EditText
    private lateinit var apellidoText: EditText
    private lateinit var correoText: EditText
    private lateinit var fechaNacimientoText: EditText
    private lateinit var actualizarBtn: Button

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_actualizar)

        nombreText = findViewById(R.id.nombre_actualizar_text)
        apellidoText = findViewById(R.id.apellido_actualizar_text)
        correoText = findViewById(R.id.correo_electronico_actualizar_text)
        fechaNacimientoText = findViewById(R.id.fecha_de_nacimiento_actualizar_text)
        actualizarBtn = findViewById(R.id.login_button)

        // Cargar datos pasados desde la vista anterior
        nombreText.setText(intent.getStringExtra("nombre"))
        apellidoText.setText(intent.getStringExtra("apellido"))
        correoText.setText(intent.getStringExtra("correo"))
        fechaNacimientoText.setText(intent.getStringExtra("fechaNacimiento"))

        actualizarBtn.setOnClickListener {
            val nuevoNombre = nombreText.text.toString().trim()
            val nuevoApellido = apellidoText.text.toString().trim()
            val nuevoCorreo = correoText.text.toString().trim()
            val nuevaFechaNacimiento = fechaNacimientoText.text.toString().trim()

            val user = auth.currentUser

            if (user != null) {
                // 🔍 Log de datos antes de actualizar
                Log.d("ActualizarUsuario", "Intentando actualizar: $nuevoNombre, $nuevoApellido, $nuevoCorreo, $nuevaFechaNacimiento")

                // 1. Verificar si el correo actual está verificado
                if (!user.isEmailVerified) {
                    // Si el correo no está verificado, enviar un correo de verificación
                    user.sendEmailVerification()
                        .addOnSuccessListener {
                            Log.d("ActualizarUsuario", "Correo de verificación enviado a ${user.email}")
                            Toast.makeText(this, "Correo de verificación enviado a ${user.email}. Por favor verifícalo antes de continuar.", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e("ActualizarUsuario", "Error al enviar verificación: ${e.message}", e)
                            Toast.makeText(this, "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // 2. Si el correo está verificado, proceder a actualizarlo
                    user.updateEmail(nuevoCorreo)
                        .addOnSuccessListener {
                            Log.d("ActualizarUsuario", "Correo electrónico actualizado correctamente")

                            // 3. Actualizar datos en Firestore
                            val userMap = mapOf(
                                "nombre" to nuevoNombre,
                                "apellido" to nuevoApellido,
                                "correo_electronico" to nuevoCorreo,
                                "fecha_de_nacimiento" to nuevaFechaNacimiento
                            )

                            db.collection("usuarios").document(user.uid)
                                .update(userMap)
                                .addOnSuccessListener {
                                    Log.d("ActualizarUsuario", "Datos del usuario actualizados en Firestore")

                                    val resultIntent = Intent().apply {
                                        putExtra("nombre", nuevoNombre)
                                        putExtra("apellido", nuevoApellido)
                                        putExtra("correo", nuevoCorreo)
                                        putExtra("fechaNacimiento", nuevaFechaNacimiento)
                                    }

                                    setResult(RESULT_OK, resultIntent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.e("ActualizarUsuario", "Error al actualizar en Firestore: ${e.message}", e)
                                    Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e("ActualizarUsuario", "Error al actualizar correo electrónico: ${e.message}", e)
                            Toast.makeText(this, "Error al actualizar correo: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Log.e("ActualizarUsuario", "Usuario no autenticado")
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        }

        val salirActualizar = findViewById<ImageView>(R.id.salida_olvido2)
        salirActualizar.setOnClickListener {
            finish()
        }
    }
}
