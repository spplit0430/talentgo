package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CostosPregunta5Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costos_pregunta5)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            return
        }

        val botonAtras = findViewById<ImageView>(R.id.atras)
        botonAtras.setOnClickListener {
            val intent = Intent(this, CostosPregunta4Activity::class.java)
            startActivity(intent)
            finish()
        }

        val opcion1 = findViewById<Button>(R.id.btn_opcion1)
        val opcion2 = findViewById<Button>(R.id.btn_opcion2)
        val opcion3 = findViewById<Button>(R.id.btn_opcion3)
        val opcion4 = findViewById<Button>(R.id.btn_opcion4)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar(opcion1.text.toString()) }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar(opcion2.text.toString()) }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar(opcion3.text.toString()) }
        opcion4.setOnClickListener { guardarRespuestaYAvanzar(opcion4.text.toString()) }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("costos_respuestas")
            .document(userId)
            .collection("preguntas")
            .document("pregunta5")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, CostosPregunta6Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
