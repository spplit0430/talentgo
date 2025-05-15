package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UniversidadesPregunta2Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universidades_pregunta2)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        val botonAtras = findViewById<ImageView>(R.id.atras)
        botonAtras.setOnClickListener {
            val intent = Intent(this, UniversidadesPregunta1Activity::class.java)
            startActivity(intent)
            finish()
        }


        val opcion1 = findViewById<Button>(R.id.button_publica)
        val opcion2 = findViewById<Button>(R.id.button_privada)
        val opcion3 = findViewById<Button>(R.id.button_online)
        val opcion4 = findViewById<Button>(R.id.button_sin_preferencia)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar("Pública") }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar("Privada") }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar("Online") }
        opcion4.setOnClickListener { guardarRespuestaYAvanzar("No tengo preferencia") }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("respuestas_universidades")
            .document(userId)
            .collection("preguntas")
            .document("pregunta2")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, UniversidadesPregunta3Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
