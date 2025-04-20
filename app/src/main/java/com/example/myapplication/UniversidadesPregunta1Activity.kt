package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UniversidadesPregunta1Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universidades_pregunta1)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish() // o redirige al login si no hay sesión
            return
        }

        // Asegúrate que estos IDs estén bien puestos en el XML
        val opcion1 = findViewById<Button>(R.id.button_colombia)
        val opcion2 = findViewById<Button>(R.id.button_hispana)
        val opcion3 = findViewById<Button>(R.id.button_usa_europa)
        val opcion4 = findViewById<Button>(R.id.button_sin_preferencia)

        opcion1.setOnClickListener { guardarRespuestaYAvanzar("Colombia") }
        opcion2.setOnClickListener { guardarRespuestaYAvanzar("En otro país de habla hispana") }
        opcion3.setOnClickListener { guardarRespuestaYAvanzar("Estados Unidos o Europa") }
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
            .document("pregunta1")
            .set(respuestaData)
            .addOnSuccessListener {
                val intent = Intent(this, UniversidadesPregunta2Activity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace() // Puedes mostrar un Toast o diálogo
            }
    }
}
