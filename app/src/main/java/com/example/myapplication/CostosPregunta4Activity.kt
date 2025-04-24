package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CostosPregunta4Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costos_pregunta4) // Asegúrate de que el XML se llame así

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        val botonAtras = findViewById<ImageView>(R.id.atras)
        botonAtras.setOnClickListener {
            val intent = Intent(this, CostosPregunta3Activity::class.java)
            startActivity(intent)
            finish()
        }

        // Botones
        val opcion1 = findViewById<Button>(R.id.btn_opcion1)
        val opcion2 = findViewById<Button>(R.id.btn_opcion2)
        val opcion3 = findViewById<Button>(R.id.btn_opcion3)

        opcion1.setOnClickListener { guardarRespuestaYMostrarResultado(opcion1.text.toString()) }
        opcion2.setOnClickListener { guardarRespuestaYMostrarResultado(opcion2.text.toString()) }
        opcion3.setOnClickListener { guardarRespuestaYMostrarResultado(opcion3.text.toString()) }
    }

    private fun guardarRespuestaYMostrarResultado(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("costos_respuestas")
            .document(userId)
            .collection("preguntas")
            .document("pregunta4")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, CostosPregunta5Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
