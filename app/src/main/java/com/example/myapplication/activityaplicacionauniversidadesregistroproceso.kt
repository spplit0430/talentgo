package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class activityaplicacionauniversidadesregistroproceso : AppCompatActivity() {

    private lateinit var etUniversidad: EditText
    private lateinit var etPrograma: EditText
    private lateinit var etFechaAplicacion: EditText

    private lateinit var btnRechazado: Button
    private lateinit var btnAprobado: Button
    private lateinit var btnEnProceso: Button
    private lateinit var btnAgregar: Button
    private lateinit var btnRetroceso: ImageView

    private var estadoSeleccionado: String = ""

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicacionauniversidades_registroproceso)


        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        etUniversidad = findViewById(R.id.et_universidad)
        etPrograma = findViewById(R.id.et_programa)
        etFechaAplicacion = findViewById(R.id.et_fecha_aplicacion)


        btnRechazado = findViewById(R.id.button_rechazado)
        btnAprobado = findViewById(R.id.button_aprobado)
        btnEnProceso = findViewById(R.id.button_en_proceso)
        btnAgregar = findViewById(R.id.btn_agregar)
        btnRetroceso = findViewById(R.id.salida_olvido2)


        etFechaAplicacion.setOnClickListener {
            mostrarFechaDialog()
        }

        btnRechazado.setOnClickListener {
            estadoSeleccionado = "Rechazado"
            mostrarMensajeEstado()
        }

        btnAprobado.setOnClickListener {
            estadoSeleccionado = "Aprobado"
            mostrarMensajeEstado()
        }

        btnEnProceso.setOnClickListener {
            estadoSeleccionado = "En Proceso"
            mostrarMensajeEstado()
        }

        btnAgregar.setOnClickListener {
            guardarEnFirestore()
        }

        btnRetroceso.setOnClickListener {
            finish()
        }
    }


    private fun mostrarFechaDialog() {
        val calendar = Calendar.getInstance()

        // Obtener la fecha actual
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, selectedHour, selectedMinute ->
                        // Formato de la fecha y hora
                        val selectedDateTime = "$selectedDay/${selectedMonth + 1}/$selectedYear $selectedHour:$selectedMinute"
                        etFechaAplicacion.setText(selectedDateTime)
                    },
                    hour, minute, true
                )
                timePickerDialog.show()
            },
            year, month, day
        )


        datePickerDialog.show()
    }

    private fun mostrarMensajeEstado() {
        Toast.makeText(this, "Seleccionaste: $estadoSeleccionado", Toast.LENGTH_SHORT).show()
    }

    private fun guardarEnFirestore() {
        val universidad = etUniversidad.text.toString().trim()
        val programa = etPrograma.text.toString().trim()
        val fechaAplicacion = etFechaAplicacion.text.toString().trim()
        val estado = estadoSeleccionado

        if (universidad.isEmpty() || programa.isEmpty() || fechaAplicacion.isEmpty() || estado.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos y selecciona un estado", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val procesoData = hashMapOf(
            "universidad" to universidad,
            "programa" to programa,
            "fechaAplicacion" to fechaAplicacion,
            "estado" to estado,
            "userId" to userId
        )


        firestore.collection("procesos_universidad")
            .add(procesoData)
            .addOnSuccessListener {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

}
