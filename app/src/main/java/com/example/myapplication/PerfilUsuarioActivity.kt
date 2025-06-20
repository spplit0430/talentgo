package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var nombreText: EditText
    private lateinit var apellidoText: EditText
    private lateinit var correoText: EditText
    private lateinit var fechaNacimientoText: EditText
    private lateinit var nombreLabel: TextView
    private lateinit var apellidoLabel: TextView

    private lateinit var actualizarUsuarioLauncher: ActivityResultLauncher<Intent>

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_usuario)


        actualizarUsuarioLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data!!
                val nuevoNombre = data.getStringExtra("nombre") ?: ""
                val nuevoApellido = data.getStringExtra("apellido") ?: ""
                val nuevoCorreo = data.getStringExtra("correo") ?: ""
                val nuevaFechaNacimiento = data.getStringExtra("fechaNacimiento") ?: ""

                nombreText.setText(nuevoNombre)
                apellidoText.setText(nuevoApellido)
                correoText.setText(nuevoCorreo)
                fechaNacimientoText.setText(nuevaFechaNacimiento)

                nombreLabel.text = nuevoNombre
                apellidoLabel.text = nuevoApellido
            }
        }


        nombreText = findViewById(R.id.nombre_text)
        apellidoText = findViewById(R.id.apellido_text)
        correoText = findViewById(R.id.correo_electronico_text)
        fechaNacimientoText = findViewById(R.id.fecha_de_nacimiento_text)


        nombreText.isEnabled = false
        apellidoText.isEnabled = false
        correoText.isEnabled = false
        fechaNacimientoText.isEnabled = false


        nombreLabel = findViewById(R.id.nombre_label)
        apellidoLabel = findViewById(R.id.apellido_label)


        auth.currentUser?.reload()?.addOnSuccessListener {
            cargarPerfilUsuario()
        }?.addOnFailureListener {
            Toast.makeText(this, "Error actualizando sesión", Toast.LENGTH_SHORT).show()
            finish()
        }


        val botonRetroceso = findViewById<ImageView>(R.id.salida_olvido3)
        botonRetroceso.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                db.collection("usuarios").document(userId).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val nombre = document.getString("nombre") ?: ""
                            val apellido = document.getString("apellido") ?: ""
                            val correo = auth.currentUser?.email ?: ""
                            val fechaNacimiento = document.getString("fechaNacimiento") ?: ""

                            val data = Intent().apply {
                                putExtra("nombre", nombre)
                                putExtra("apellido", apellido)
                                putExtra("correo", correo)
                                putExtra("fechaNacimiento", fechaNacimiento)
                            }
                            setResult(RESULT_OK, data)
                            finish()
                        } else {
                            Toast.makeText(this, "No se encontró el perfil", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        }


        val actualizarBtn = findViewById<Button>(R.id.login_button)
        actualizarBtn.setOnClickListener {
            val intent = Intent(this, ActualizarUsuarioActivity::class.java).apply {
                putExtra("nombre", nombreText.text.toString())
                putExtra("apellido", apellidoText.text.toString())
                putExtra("correo", correoText.text.toString())
                putExtra("fechaNacimiento", fechaNacimientoText.text.toString())
            }
            actualizarUsuarioLauncher.launch(intent)
        }
    }

    private fun cargarPerfilUsuario() {
        val userId = auth.currentUser?.uid
        val correoActualizado = auth.currentUser?.email

        if (userId != null && correoActualizado != null) {
            db.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombre = document.getString("nombre") ?: ""
                        val apellido = document.getString("apellido") ?: ""
                        val fechaNacimientoString = document.getString("fechaNacimiento") ?: ""

                        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        val fechaNacimiento = try {
                            val date = dateFormat.parse(fechaNacimientoString)
                            val displayFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                            displayFormat.format(date)
                        } catch (e: Exception) {
                            "Fecha inválida"
                        }

                        nombreText.setText(nombre)
                        apellidoText.setText(apellido)
                        correoText.setText(correoActualizado)
                        fechaNacimientoText.setText(fechaNacimiento)

                        nombreLabel.text = nombre
                        apellidoLabel.text = apellido
                    } else {
                        Toast.makeText(this, "No se encontró el perfil", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
