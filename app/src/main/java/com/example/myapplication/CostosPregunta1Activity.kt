package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CostosPregunta1Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costos_pregunta1)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish() // O redirige al login si no hay sesión
            return
        }

        // Botones de respuesta
        val opcion1 = findViewById<Button>(R.id.btn_opcion1)
        val opcion2 = findViewById<Button>(R.id.btn_opcion2)
        val opcion3 = findViewById<Button>(R.id.btn_opcion3)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar(opcion1.text.toString()) }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar(opcion2.text.toString()) }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar(opcion3.text.toString()) }
    }

    private fun guardarRespuestaYAvanzar(respuesta: String) {
        val userId = auth.currentUser?.uid ?: return

        val respuestaData = hashMapOf(
            "respuesta" to respuesta
        )

        firestore.collection("costos_respuestas")
            .document(userId)
            .collection("preguntas")
            .document("pregunta1")
            .set(respuestaData)
            .addOnSuccessListener {
                // Redirigir a la siguiente pregunta
                val intent = Intent(this, CostosPregunta2Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
