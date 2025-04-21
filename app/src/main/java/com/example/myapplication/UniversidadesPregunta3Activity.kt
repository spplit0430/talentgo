package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UniversidadesPregunta3Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universidades_pregunta3)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish() // o redirige al login si no hay sesión
            return
        }

        val opcion1 = findViewById<Button>(R.id.button_ciencias)
        val opcion2 = findViewById<Button>(R.id.button_artes)
        val opcion3 = findViewById<Button>(R.id.button_negocios)
        val opcion4 = findViewById<Button>(R.id.button_salud)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar("Ciencias e ingeniería") }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar("Artes y humanidades") }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar("Negocios y economía") }
        opcion4.setOnClickListener { guardarRespuestaYAvanzar("Salud y medicina") }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("respuestas_universidades")
            .document(userId)
            .collection("preguntas")
            .document("pregunta3")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, UniversidadesPregunta4Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
