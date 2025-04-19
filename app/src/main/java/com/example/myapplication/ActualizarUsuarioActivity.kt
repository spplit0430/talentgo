package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

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

        // Cargar datos desde la pantalla anterior
        val correoActual = intent.getStringExtra("correo") ?: ""
        nombreText.setText(intent.getStringExtra("nombre"))
        apellidoText.setText(intent.getStringExtra("apellido"))
        correoText.setText(correoActual)
        fechaNacimientoText.setText(intent.getStringExtra("fechaNacimiento"))

        actualizarBtn.setOnClickListener {
            val nuevoNombre = nombreText.text.toString().trim()
            val nuevoApellido = apellidoText.text.toString().trim()
            val nuevoCorreo = correoText.text.toString().trim()
            val nuevaFechaNacimiento = fechaNacimientoText.text.toString().trim()
            val user = auth.currentUser

            if (user != null) {
                val userMap = mapOf(
                    "nombre" to nuevoNombre,
                    "apellido" to nuevoApellido,
                    "fecha_de_nacimiento" to nuevaFechaNacimiento
                )

                // ⚠️ Solo si el correo cambió
                if (nuevoCorreo != correoActual) {
                    // Pedir contraseña para reautenticación
                    val input = EditText(this).apply {
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        hint = "Contraseña actual"
                    }

                    AlertDialog.Builder(this)
                        .setTitle("Verificación requerida")
                        .setMessage("Ingresa tu contraseña para cambiar el correo")
                        .setView(input)
                        .setPositiveButton("Confirmar") { _, _ ->
                            val password = input.text.toString()
                            val credential = EmailAuthProvider.getCredential(correoActual, password)

                            user.reauthenticate(credential).addOnSuccessListener {
                                user.verifyBeforeUpdateEmail(nuevoCorreo)
                                    .addOnSuccessListener {
                                        Log.d("ActualizarUsuario", "Correo nuevo: $nuevoCorreo, verificación enviada.")
                                        Toast.makeText(this, "Verificación enviada a $nuevoCorreo. Debes confirmarla para completar el cambio.", Toast.LENGTH_LONG).show()

                                        // Actualizar Firestore excepto correo (se actualiza después de verificación)
                                        db.collection("usuarios").document(user.uid)
                                            .update(userMap)
                                            .addOnSuccessListener {
                                                Log.d("ActualizarUsuario", "Datos actualizados (excepto correo).")

                                                // Redirigir al LoginActivity después de enviar la verificación
                                                val intent = Intent(this, LoginActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("ActualizarUsuario", "Error al solicitar verificación: ${e.message}", e)
                                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            }.addOnFailureListener { e ->
                                Log.e("ActualizarUsuario", "Error reautenticando: ${e.message}", e)
                                Toast.makeText(this, "Contraseña incorrecta o sesión caducada", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                } else {
                    // Si no se cambia el correo, solo actualiza los demás datos
                    userMap.plus("correo_electronico" to correoActual) // por si quieres mantenerlo igual en Firestore
                    db.collection("usuarios").document(user.uid)
                        .update(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()

                            val resultIntent = Intent().apply {
                                putExtra("nombre", nuevoNombre)
                                putExtra("apellido", nuevoApellido)
                                putExtra("correo", correoActual)
                                putExtra("fechaNacimiento", nuevaFechaNacimiento)
                            }

                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("ActualizarUsuario", "Error actualizando Firestore: ${e.message}", e)
                            Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.salida_olvido2).setOnClickListener {
            finish()
        }
    }
}
