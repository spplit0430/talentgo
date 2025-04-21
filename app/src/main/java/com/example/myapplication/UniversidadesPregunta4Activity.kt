package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.widget.Toast

class UniversidadesPregunta4Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universidades_pregunta4)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        val opcion1 = findViewById<Button>(R.id.button_prestigio)
        val opcion2 = findViewById<Button>(R.id.button_costo)
        val opcion3 = findViewById<Button>(R.id.button_ubicacion)
        val opcion4 = findViewById<Button>(R.id.button_flexibilidad)

        opcion1.setOnClickListener { guardarRespuestaYFinalizar("Prestigio y reconocimiento") }
        opcion2.setOnClickListener { guardarRespuestaYFinalizar("Costo de matrícula y becas") }
        opcion3.setOnClickListener { guardarRespuestaYFinalizar("Ubicación y cercanía") }
        opcion4.setOnClickListener { guardarRespuestaYFinalizar("Flexibilidad en horarios") }
    }

    private fun guardarRespuestaYFinalizar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("respuestas_universidades")
            .document(userId)
            .collection("preguntas")
            .document("pregunta4")
            .set(respuestaData)
            .addOnSuccessListener {
                Toast.makeText(this, "Gracias por completar el test!", Toast.LENGTH_SHORT).show()
                // Aquí puedes redirigir al resumen de resultados o pantalla principal
                val intent = Intent(this, ResultadoUniversidadActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar la respuesta", Toast.LENGTH_SHORT).show()
            }
    }
}
