package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VocacionPregunta10Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocacion_pregunta10)

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

        opcion1.setOnClickListener { guardarRespuestaYFinalizar("Haber creado una empresa o un invento innovador") }
        opcion2.setOnClickListener { guardarRespuestaYFinalizar("Haber escrito un libro, dirigido una película o diseñado algo icónico") }
        opcion3.setOnClickListener { guardarRespuestaYFinalizar("Haber cambiado la vida de muchas personas a través de mi trabajo") }
        opcion4.setOnClickListener { guardarRespuestaYFinalizar("Haber dirigido un equipo exitoso y alcanzado grandes metas") }
    }

    private fun guardarRespuestaYFinalizar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("respuestas_vocacion")
            .document(userId)
            .collection("preguntas")
            .document("pregunta10")
            .set(respuestaData)
            .addOnSuccessListener {
                // Aquí puedes redirigir a un resumen o pantalla de resultados
                val intent = Intent(this, ResultadoVocacionalActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
