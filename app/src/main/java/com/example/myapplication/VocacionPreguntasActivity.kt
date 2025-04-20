package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VocacionPreguntasActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocacion_pregunta1)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish() // O redirige al login si no hay sesión
            return
        }

        // Botones de respuesta
        val opcion1 = findViewById<Button>(R.id.opcion1)
        val opcion2 = findViewById<Button>(R.id.opcion2)
        val opcion3 = findViewById<Button>(R.id.opcion3)
        val opcion4 = findViewById<Button>(R.id.opcion4)
        val opcion5 = findViewById<Button>(R.id.opcion5)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar("Resolver problemas y acertijos") }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar("Crear arte o contenido") }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar("Ayudar a los demás") }
        opcion4.setOnClickListener { guardarRespuestaYAvanzar("Organizar y planificar") }
        opcion5.setOnClickListener { guardarRespuestaYAvanzar("Explorar nuevas ideas y conceptos") }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        // Estructura para guardar la respuesta en un documento específico por pregunta
        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        // Guarda la respuesta bajo la colección "respuestas_vocacion" con un subdocumento para cada pregunta
        firestore.collection("respuestas_vocacion")
            .document(userId)  // Usamos el UID del usuario como documento
            .collection("preguntas")  // Subcolección "preguntas" para cada pregunta
            .document("pregunta1")  // Documento específico para la primera pregunta
            .set(respuestaData)
            .addOnSuccessListener {
                // Redirigir a la siguiente pregunta
                val intent = Intent(this, VocacionPregunta2Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Manejar error (puedes mostrar un Toast si quieres)
                e.printStackTrace()
            }
    }
}
