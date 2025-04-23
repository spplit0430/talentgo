package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResultadoCostoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var resultadoPrincipal: TextView
    private lateinit var descripcion: TextView
    private lateinit var imagenResultado: ImageView
    private lateinit var finalizarBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultado_costos)

        resultadoPrincipal = findViewById(R.id.resultado_principal)
        descripcion = findViewById(R.id.descripcion)
        imagenResultado = findViewById(R.id.imagen_resultado)
        finalizarBtn = findViewById(R.id.btn_finalizar)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid ?: return

        firestore.collection("costos_respuestas")
            .document(userId)
            .collection("preguntas")
            .get()
            .addOnSuccessListener { result ->
                val conteo = mutableMapOf(
                    "presupuestoBajo" to 0,
                    "becaImportante" to 0,
                    "alternativaEconomica" to 0,
                    "moderado" to 0
                )

                for (doc in result) {
                    val respuesta = doc.getString("respuesta") ?: continue
                    Log.d("ResultadoCostoActivity", "Respuesta: $respuesta")

                    when {
                        respuesta.contains("1 millón a 3 millones", true) -> conteo["presupuestoBajo"] = conteo["presupuestoBajo"]!! + 1
                        respuesta.contains("3 millones a 7 millones", true) -> conteo["becaImportante"] = conteo["becaImportante"]!! + 1
                        respuesta.contains("7 millones a superior", true) -> conteo["alternativaEconomica"] = conteo["alternativaEconomica"]!! + 1
                        respuesta.contains("sería de gran ayuda", true) || respuesta.contains("puedo costear", true) -> conteo["moderado"] = conteo["moderado"]!! + 1
                        respuesta.contains("buscar becas", true) -> conteo["presupuestoBajo"] = conteo["presupuestoBajo"]!! + 1
                        respuesta.contains("optar por una universidad económica", true) -> conteo["moderado"] = conteo["moderado"]!! + 1
                        respuesta.contains("asumir el costo con apoyo", true) -> conteo["alternativaEconomica"] = conteo["alternativaEconomica"]!! + 1
                    }
                }

                val perfilDominante = conteo.maxByOrNull { it.value }?.key

                val nombrePerfil = when (perfilDominante) {
                    "presupuestoBajo" -> {
                        mostrarResultado("Perfil Económico", "Tu principal preocupación es el costo de la educación. Prefieres opciones económicas como becas y universidades públicas.", R.drawable.economica)
                        "Perfil Económico"
                    }
                    "becaImportante" -> {
                        mostrarResultado("Perfil con Beca", "Buscas oportunidades de becas y ayudas económicas para estudiar, prefiriendo universidades con programas de apoyo.", R.drawable.brujula)
                        "Perfil con Beca"
                    }
                    "alternativaEconomica" -> {
                        mostrarResultado("Perfil de Alta Inversión", "Estás dispuesto a invertir más en tu educación, buscando opciones con prestigio y mayor inversión en materiales y formación.", R.drawable.idea)
                        "Perfil de Alta Inversión"
                    }
                    "moderado" -> {
                        mostrarResultado("Perfil Moderado", "Valoras la flexibilidad y el costo. Buscas opciones accesibles que te permitan adaptar tu educación a tus necesidades.", R.drawable.prestigio)
                        "Perfil Moderado"
                    }
                    else -> {
                        resultadoPrincipal.text = "Perfil de costos desconocido"
                        return@addOnSuccessListener
                    }
                }

                // Guardar resultado en Firestore (colección metricas_costos)
                val metricas = hashMapOf(
                    "presupuestoBajo" to conteo["presupuestoBajo"],
                    "becaImportante" to conteo["becaImportante"],
                    "alternativaEconomica" to conteo["alternativaEconomica"],
                    "moderado" to conteo["moderado"]
                )

                firestore.collection("metricas_costos")
                    .document(userId)
                    .set(metricas)
                    .addOnSuccessListener {
                        Log.d("ResultadoCostoActivity", "Métricas de costos guardadas exitosamente")
                    }
                    .addOnFailureListener { e ->
                        Log.e("ResultadoCostoActivity", "Error al guardar métricas de costos", e)
                    }
            }
            .addOnFailureListener { exception ->
                resultadoPrincipal.text = "Error al cargar los datos."
            }

        finalizarBtn.setOnClickListener {
            val intent = Intent(this, ContinuaCostosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarResultado(titulo: String, desc: String, imagenRes: Int) {
        resultadoPrincipal.text = titulo
        descripcion.text = desc
        imagenResultado.setImageResource(imagenRes)
    }
}
