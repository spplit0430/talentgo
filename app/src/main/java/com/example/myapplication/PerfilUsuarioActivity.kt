package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var nombreText: EditText
    private lateinit var apellidoText: EditText
    private lateinit var correoText: EditText
    private lateinit var fechaNacimientoText: EditText
    private lateinit var nombreLabel: TextView
    private lateinit var apellidoLabel: TextView

    private lateinit var actualizarUsuarioLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_usuario)

        // Inicializar ActivityResultLauncher
        actualizarUsuarioLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data!!
                val nuevoNombre = data.getStringExtra("nombre") ?: ""
                val nuevoApellido = data.getStringExtra("apellido") ?: ""
                val nuevoCorreo = data.getStringExtra("correo") ?: ""
                val nuevaFechaNacimiento = data.getStringExtra("fechaNacimiento") ?: ""

                // Actualizar los campos con los nuevos valores
                nombreText.setText(nuevoNombre)
                apellidoText.setText(nuevoApellido)
                correoText.setText(nuevoCorreo)
                fechaNacimientoText.setText(nuevaFechaNacimiento)

                nombreLabel.text = nuevoNombre
                apellidoLabel.text = nuevoApellido
            }
        }

        // Referencias a los EditText
        nombreText = findViewById(R.id.nombre_text)
        apellidoText = findViewById(R.id.apellido_text)
        correoText = findViewById(R.id.correo_electronico_text)
        fechaNacimientoText = findViewById(R.id.fecha_de_nacimiento_text)

        // Deshabilitar edición directa en los campos
        nombreText.isEnabled = false
        apellidoText.isEnabled = false
        correoText.isEnabled = false
        fechaNacimientoText.isEnabled = false


        // Referencias a los TextView del encabezado
        nombreLabel = findViewById(R.id.nombre_label)
        apellidoLabel = findViewById(R.id.apellido_label)

        // Obtener datos desde el intent
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""
        val correo = intent.getStringExtra("correo") ?: ""
        val fechaNacimiento = intent.getStringExtra("fechaNacimiento") ?: ""

        // Establecer valores en los campos
        nombreText.setText(nombre)
        apellidoText.setText(apellido)
        correoText.setText(correo)
        fechaNacimientoText.setText(fechaNacimiento)
        nombreLabel.text = nombre
        apellidoLabel.text = apellido

        // Botón de retroceso
        val botonRetroceso = findViewById<ImageView>(R.id.salida_olvido3)
        botonRetroceso.setOnClickListener {
            val data = Intent().apply {
                putExtra("nombre", nombreText.text.toString())
                putExtra("apellido", apellidoText.text.toString())
                putExtra("correo", correoText.text.toString())
                putExtra("fechaNacimiento", fechaNacimientoText.text.toString())
            }
            setResult(RESULT_OK, data)
            finish()
        }

        // Botón de actualizar información
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
}
