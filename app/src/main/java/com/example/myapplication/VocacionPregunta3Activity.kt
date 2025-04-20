package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VocacionPregunta3Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocacion_pregunta3) // Asegúrate que este layout exista

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        // Botones de opción
        val opcion1 = findViewById<Button>(R.id.opcion1)
        val opcion2 = findViewById<Button>(R.id.opcion2)
        val opcion3 = findViewById<Button>(R.id.opcion3)
        val opcion4 = findViewById<Button>(R.id.opcion4)
        val opcion5 = findViewById<Button>(R.id.opcion5)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar("Seguridad y estabilidad") }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar("Creatividad y libertad") }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar("Impacto en la sociedad") }
        opcion4.setOnClickListener { guardarRespuestaYAvanzar("Innovación y descubrimiento") }
        opcion5.setOnClickListener { guardarRespuestaYAvanzar("Buen sueldo y crecimiento profesional") }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("respuestas_vocacion")
            .document(userId)
            .collection("preguntas")
            .document("pregunta3")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, VocacionPregunta4Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
