package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AgendamientoMisCitasActivity : AppCompatActivity() {

    private lateinit var containerCitas: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamiento_miscitas)

        containerCitas = findViewById(R.id.container_citas)

        cargarCitas()
    }

    private fun cargarCitas() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("citas")
            .whereEqualTo("usuarioId", userId)
            .get()
            .addOnSuccessListener { result ->
                containerCitas.removeAllViews()

                if (result.isEmpty) {
                    val noCitas = TextView(this).apply {
                        text = "No tienes citas agendadas."
                        textSize = 18f
                        setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    }
                    containerCitas.addView(noCitas)
                } else {
                    var contador = 1
                    for (document in result) {
                        val universidad = document.getString("universidad") ?: ""
                        val fechaHora = document.getString("fechaHora") ?: ""
                        val tipoReunion = document.getString("tipoReunion") ?: ""
                        val recordatorios = document.getBoolean("recordatorios") ?: false

                        val cardView = CardView(this).apply {
                            radius = 24f
                            cardElevation = 12f
                            useCompatPadding = true
                            setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))

                            val layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, 0, 32) // Espacio entre tarjetas
                            }
                            this.layoutParams = layoutParams
                        }

                        val innerLayout = LinearLayout(this).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(24, 24, 24, 24)

                            val title = TextView(context).apply {
                                text = "🗓️ Cita $contador"
                                textSize = 22f
                                setTextColor(ContextCompat.getColor(context, R.color.teal_700))
                                setPadding(0, 0, 0, 8)
                            }

                            val universidadView = TextView(context).apply {
                                text = "📚 Universidad: $universidad"
                                textSize = 16f
                            }

                            val fechaView = TextView(context).apply {
                                text = "📆 Fecha y hora: $fechaHora"
                                textSize = 16f
                            }

                            val tipoView = TextView(context).apply {
                                text = "💼 Tipo de reunión: $tipoReunion"
                                textSize = 16f
                            }

                            val recordatorioView = TextView(context).apply {
                                text = "🔔 Recordatorios: ${if (recordatorios) "Activados" else "Desactivados"}"
                                textSize = 16f
                            }

                            val btnDetalles = Button(context).apply {
                                text = "Ver Detalles"
                                setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
                                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                                setOnClickListener {
                                    mostrarDetalleCita(universidad, fechaHora, tipoReunion, recordatorios)
                                }
                            }

                            addView(title)
                            addView(universidadView)
                            addView(fechaView)
                            addView(tipoView)
                            addView(recordatorioView)
                            addView(btnDetalles)
                        }

                        cardView.addView(innerLayout)
                        containerCitas.addView(cardView)
                        contador++
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar citas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDetalleCita(
        universidad: String,
        fechaHora: String,
        tipoReunion: String,
        recordatorios: Boolean
    ) {
        val mensaje = """
            📚 Universidad: $universidad
            🗓️ Fecha y Hora: $fechaHora
            🧑‍💼 Tipo de Reunión: $tipoReunion
            🔔 Recordatorios: ${if (recordatorios) "Activados" else "Desactivados"}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Detalle de la Cita")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
