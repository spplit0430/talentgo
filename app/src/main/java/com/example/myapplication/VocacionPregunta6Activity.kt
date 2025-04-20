package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VocacionPregunta6Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocacion_pregunta6)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        val opcion1 = findViewById<Button>(R.id.opcion1)
        val opcion2 = findViewById<Button>(R.id.opcion2)
        val opcion3 = findViewById<Button>(R.id.opcion3)
        val opcion4 = findViewById<Button>(R.id.opcion4)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar("Matemáticas o ciencias") }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar("Arte, música o literatura") }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar("Historia o ciencias sociales") }
        opcion4.setOnClickListener { guardarRespuestaYAvanzar("Educación física o actividades prácticas") }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("respuestas_vocacion")
            .document(userId)
            .collection("preguntas")
            .document("pregunta6")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, VocacionPregunta7Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
