package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AgendamientoCancelarCitasActivity : AppCompatActivity() {

    private lateinit var containerCitas: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamiento_cancelarcitas)

        containerCitas = findViewById(R.id.container_citas)

        cargarCitas()


        val botonAtras = findViewById<ImageView>(R.id.salida_olvido2)
        botonAtras.setOnClickListener {
            val intent = Intent(this, AgendamientoActivity::class.java)
            startActivity(intent)
            finish()
        }
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
                        val citaId = document.id
                        val universidad = document.getString("universidad") ?: ""
                        val fechaHora = document.getString("fechaHora") ?: ""
                        val tipoReunion = document.getString("tipoReunion") ?: ""

                        val cardView = CardView(this).apply {
                            radius = 24f
                            cardElevation = 12f
                            useCompatPadding = true
                            setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))

                            val layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, 0, 32)
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

                            val btnCancelar = Button(context).apply {
                                text = "Cancelar cita"
                                setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                                setOnClickListener {
                                    confirmarCancelacion(citaId)
                                }
                            }

                            addView(title)
                            addView(universidadView)
                            addView(fechaView)
                            addView(tipoView)
                            addView(btnCancelar)
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

    private fun confirmarCancelacion(citaId: String) {
        AlertDialog.Builder(this)
            .setTitle("Cancelar cita")
            .setMessage("¿Estás seguro de que deseas cancelar esta cita?")
            .setPositiveButton("Sí, cancelar") { _, _ ->
                firestore.collection("citas").document(citaId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cita cancelada exitosamente.", Toast.LENGTH_SHORT).show()
                        cargarCitas() // Recarga la lista
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al cancelar cita: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
