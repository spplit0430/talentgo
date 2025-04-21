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
                // Definir los contadores de cada perfil
                val conteo = mutableMapOf(
                    "presupuestoBajo" to 0,
                    "becaImportante" to 0,
                    "alternativaEconomica" to 0,
                    "flexible" to 0
                )

                for (doc in result) {
                    val respuesta = doc.getString("respuesta") ?: continue
                    Log.d("ResultadoCostoActivity", "Respuesta: $respuesta")  // Agregar log para ver las respuestas
                    when {
                        respuesta.contains("1 millón a 3 millones", true) -> conteo["presupuestoBajo"] = conteo["presupuestoBajo"]!! + 1
                        respuesta.contains("3 millones a 7 millones", true) -> conteo["becaImportante"] = conteo["becaImportante"]!! + 1
                        respuesta.contains("7 millones a superior", true) -> conteo["alternativaEconomica"] = conteo["alternativaEconomica"]!! + 1
                        respuesta.contains("sería de gran ayuda", true) || respuesta.contains("puedo costear", true) -> conteo["flexible"] = conteo["flexible"]!! + 1
                        respuesta.contains("buscar becas", true) -> conteo["presupuestoBajo"] = conteo["presupuestoBajo"]!! + 1
                        respuesta.contains("optar por una universidad económica", true) -> conteo["flexible"] = conteo["flexible"]!! + 1
                        respuesta.contains("asumir el costo con apoyo", true) -> conteo["alternativaEconomica"] = conteo["alternativaEconomica"]!! + 1
                    }
                }


                // Determinar el perfil basado en los conteos
                val perfilDominante = conteo.maxByOrNull { it.value }?.key

                when (perfilDominante) {
                    "presupuestoBajo" -> mostrarResultado(
                        "Perfil Económico",
                        "Tu principal preocupación es el costo de la educación. Prefieres opciones económicas como becas y universidades públicas.",
                        R.drawable.economica
                    )
                    "becaImportante" -> mostrarResultado(
                        "Perfil con Beca",
                        "Buscas oportunidades de becas y ayudas económicas para estudiar, prefiriendo universidades con programas de apoyo.",
                        R.drawable.brujula
                    )
                    "alternativaEconomica" -> mostrarResultado(
                        "Perfil de Alta Inversión",
                        "Estás dispuesto a invertir más en tu educación, buscando opciones con prestigio y mayor inversión en materiales y formación.",
                        R.drawable.idea
                    )
                    "flexible" -> mostrarResultado(
                        "Perfil Flexible",
                        "Valoras la flexibilidad y el costo. Buscas opciones accesibles que te permitan adaptar tu educación a tus necesidades.",
                        R.drawable.prestigio
                    )
                    else -> throw IllegalArgumentException("Perfil de costos desconocido: $perfilDominante")
                }
            }
            .addOnFailureListener { exception ->
                // En caso de error al obtener las respuestas
                resultadoPrincipal.text = "Error al cargar los datos."
            }

        finalizarBtn.setOnClickListener {
            // Crear un Intent para redirigir a la actividad principal
            val intent = Intent(this, MenuPrincipalActivity::class.java)
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
