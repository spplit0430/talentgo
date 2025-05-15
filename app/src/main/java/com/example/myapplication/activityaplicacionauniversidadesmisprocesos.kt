package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class activityaplicacionauniversidadesmisprocesos : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicacionauniversidades_misprocesos)

        val contenedorTarjetas = findViewById<LinearLayout>(R.id.contenedor_tarjetas)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        obtenerProcesos(contenedorTarjetas)

        val btnRetroceso = findViewById<ImageView>(R.id.salida_olvido2)
        btnRetroceso.setOnClickListener {
            finish()
        }
    }

    private fun obtenerProcesos(contenedorTarjetas: LinearLayout) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("procesos_universidad")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    contenedorTarjetas.removeAllViews()
                    for (document in documents) {
                        val nombre = document.getString("universidad") ?: "No Disponible"
                        val fecha = document.getString("fechaAplicacion") ?: "No Disponible"
                        val estado = document.getString("estado") ?: "No Disponible"
                        val tarjeta = crearTarjeta(nombre, fecha, estado, document.id)
                        contenedorTarjetas.addView(tarjeta)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al obtener los procesos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun crearTarjeta(nombreUni: String, fecha: String, estado: String, documentId: String): View {
        val context = this

        val tarjeta = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 12, 12, 12)
            setBackgroundColor(Color.parseColor("#5B97B1"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 12
                marginStart = 4
                marginEnd = 4
            }
            elevation = 4f
        }

        val nombreTextView = TextView(context).apply {
            text = nombreUni
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            textSize = 16f
        }

        val fechaTextView = TextView(context).apply {
            text = fecha
            setTextColor(Color.WHITE)
            textSize = 12f
            setPadding(0, 4, 0, 0)
        }

        val estadoConEmoji = when {
            estado.contains("Aprobado", ignoreCase = true) -> "😄 $estado"
            estado.contains("Rechazado", ignoreCase = true) -> "😢 $estado"
            else -> "😐 $estado"
        }

        val estadoTextView = TextView(context).apply {
            text = estadoConEmoji
            textSize = 14f
            setTypeface(null, Typeface.BOLD)
            setTextColor(
                when {
                    estado.contains("Aprobado", true) -> Color.parseColor("#00FF00")
                    estado.contains("Rechazado", true) -> Color.parseColor("#FF0000")
                    else -> Color.parseColor("#122CCC")
                }
            )
        }

        val botonesLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL

            val editar = ImageView(context).apply {
                setImageResource(R.drawable.editar)
                layoutParams = LinearLayout.LayoutParams(24.dpToPx(), 24.dpToPx()).apply {
                    marginEnd = 10
                }
            }

            val eliminar = ImageView(context).apply {
                setImageResource(R.drawable.x)
                layoutParams = LinearLayout.LayoutParams(24.dpToPx(), 24.dpToPx()).apply {
                    marginEnd = 4
                }
            }

            addView(editar)
            addView(eliminar)

            editar.setOnClickListener {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editar_proceso, null)
                val editUni = dialogView.findViewById<EditText>(R.id.editTextUniversidad)
                val editFecha = dialogView.findViewById<EditText>(R.id.editTextFecha)

                var estadoSeleccionado = estado


                val btnRechazado = dialogView.findViewById<Button>(R.id.btnRechazado)
                val btnAprobado = dialogView.findViewById<Button>(R.id.btnAprobado)
                val btnProceso = dialogView.findViewById<Button>(R.id.btnEnProceso)

                btnRechazado.setOnClickListener {
                    estadoSeleccionado = "Rechazado"
                }

                btnAprobado.setOnClickListener {
                    estadoSeleccionado = "Aprobado"
                }

                btnProceso.setOnClickListener {
                    estadoSeleccionado = "En Proceso"
                }

                editUni.setText(nombreUni)
                editFecha.setText(fecha)


                editFecha.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, selectedYear, selectedMonth, selectedDay ->

                            val timePickerDialog = TimePickerDialog(
                                context,
                                { _, selectedHour, selectedMinute ->

                                    val selectedDateTime = "$selectedDay/${selectedMonth + 1}/$selectedYear $selectedHour:$selectedMinute"
                                    editFecha.setText(selectedDateTime)
                                },
                                hour, minute, true
                            )
                            timePickerDialog.show()
                        },
                        year, month, day
                    )
                    datePickerDialog.show()
                }

                val dialog = android.app.AlertDialog.Builder(context)
                    .setView(dialogView)
                    .create()


                dialogView.findViewById<Button>(R.id.btn_cancelar)?.setOnClickListener {
                    dialog.dismiss()
                }

                dialogView.findViewById<Button>(R.id.btn_guardar)?.setOnClickListener {
                    val nuevaUni = editUni.text.toString()
                    val nuevaFecha = editFecha.text.toString()

                    val nuevosDatos = mapOf(
                        "universidad" to nuevaUni,
                        "fechaAplicacion" to nuevaFecha,
                        "estado" to estadoSeleccionado
                    )

                    firestore.collection("procesos_universidad").document(documentId)
                        .update(nuevosDatos)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Proceso actualizado", Toast.LENGTH_SHORT).show()
                            obtenerProcesos(findViewById(R.id.contenedor_tarjetas))
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                }

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()
            }

            eliminar.setOnClickListener {
                firestore.collection("procesos_universidad").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Proceso eliminado", Toast.LENGTH_SHORT).show()
                        obtenerProcesos(findViewById(R.id.contenedor_tarjetas))
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        tarjeta.addView(nombreTextView)
        tarjeta.addView(fechaTextView)
        tarjeta.addView(estadoTextView)
        tarjeta.addView(botonesLayout)

        return tarjeta
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
