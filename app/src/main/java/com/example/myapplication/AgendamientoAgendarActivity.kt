package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AgendamientoAgendarActivity : AppCompatActivity() {

    private lateinit var inputUniversidad: EditText
    private lateinit var inputFechaHora: EditText
    private lateinit var btnRemoto: Button
    private lateinit var btnPresencial: Button
    private lateinit var switchRecordatorios: Switch
    private lateinit var btnAgendar: Button

    private var tipoReunion: String = ""

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamiento_agendar)

        // Botón de retroceso
        val botonAtras = findViewById<ImageView>(R.id.salida_olvido2)
        botonAtras.setOnClickListener {
            val intent = Intent(this, AgendamientoActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Referencias a los elementos
        inputUniversidad = findViewById(R.id.input_universidad)
        inputFechaHora = findViewById(R.id.input_fecha_hora)
        btnRemoto = findViewById(R.id.btn_remoto)
        btnPresencial = findViewById(R.id.btn_presencial)
        switchRecordatorios = findViewById(R.id.switch_recordatorios)
        btnAgendar = findViewById(R.id.btn_agendar)

        // Selector de fecha y hora
        inputFechaHora.setOnClickListener {
            mostrarDateTimePicker()
        }

        btnRemoto.setOnClickListener {
            tipoReunion = "Remoto"
            btnRemoto.setBackgroundTintList(getColorStateList(R.color.teal_700))
            btnPresencial.setBackgroundTintList(getColorStateList(R.color.gray))
        }

        btnPresencial.setOnClickListener {
            tipoReunion = "Presencial"
            btnPresencial.setBackgroundTintList(getColorStateList(R.color.teal_700))
            btnRemoto.setBackgroundTintList(getColorStateList(R.color.gray))
        }

        btnAgendar.setOnClickListener {
            guardarCitaEnFirestore()
        }
    }

    private fun mostrarDateTimePicker() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                inputFechaHora.setText(formato.format(calendar.time))

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // Obtener la fecha de hoy
        val today = Calendar.getInstance()

        // Establecer la fecha mínima del DatePicker como la fecha de hoy
        val datePickerDialog = DatePickerDialog(
            this, dateListener,
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        // Establecer la fecha mínima como la de hoy
        datePickerDialog.datePicker.minDate = today.timeInMillis

        datePickerDialog.show()
    }

    private fun guardarCitaEnFirestore() {
        val universidad = inputUniversidad.text.toString().trim()
        val fechaHora = inputFechaHora.text.toString().trim()
        val recordatorios = switchRecordatorios.isChecked
        val usuarioId = auth.currentUser?.uid

        if (universidad.isEmpty() || fechaHora.isEmpty() || tipoReunion.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (usuarioId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val cita = hashMapOf(
            "usuarioId" to usuarioId,
            "universidad" to universidad,
            "fechaHora" to fechaHora,
            "tipoReunion" to tipoReunion,
            "recordatorios" to recordatorios,
            "timestamp" to Timestamp.now()
        )

        firestore.collection("citas")
            .add(cita)
            .addOnSuccessListener {
                Toast.makeText(this, "Cita agendada exitosamente", Toast.LENGTH_LONG).show()

                // Redirigir a AgendamientoActivity
                val intent = Intent(this, AgendamientoActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar la cita: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
