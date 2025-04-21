package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResultadoVocacionalActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var resultadoPrincipal: TextView
    private lateinit var descripcion: TextView
    private lateinit var imagenResultado: ImageView
    private lateinit var finalizarBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultado_vocacion)

        resultadoPrincipal = findViewById(R.id.resultado_principal)
        descripcion = findViewById(R.id.descripcion)
        imagenResultado = findViewById(R.id.imagen_resultado)
        finalizarBtn = findViewById(R.id.btn_finalizar)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid ?: return

        firestore.collection("respuestas_vocacion")
            .document(userId)
            .collection("preguntas")
            .get()
            .addOnSuccessListener { result ->
                val conteo = mutableMapOf(
                    "cientifico" to 0,
                    "artistico" to 0,
                    "social" to 0,
                    "empresarial" to 0
                )

                for (doc in result) {
                    val respuesta = doc.getString("respuesta") ?: continue

                    when {
                        respuesta.contains("problemas", true) || respuesta.contains("ciencia", true) -> conteo["cientifico"] = conteo["cientifico"]!! + 1
                        respuesta.contains("arte", true) || respuesta.contains("creativo", true) || respuesta.contains("diseñado", true) -> conteo["artistico"] = conteo["artistico"]!! + 1
                        respuesta.contains("personas", true) || respuesta.contains("ayudar", true) -> conteo["social"] = conteo["social"]!! + 1
                        respuesta.contains("empresa", true) || respuesta.contains("liderado", true) -> conteo["empresarial"] = conteo["empresarial"]!! + 1
                    }
                }

                val perfilDominante = conteo.maxByOrNull { it.value }?.key

                when (perfilDominante) {
                    "cientifico" -> mostrarResultado(
                        "Perfil Analítico",
                        "Eres una persona curiosa, lógica y resolutiva. Ideal para carreras como Ingeniería, Matemáticas, Física o Ciencias de la Computación.",
                        R.drawable.vocacion1
                    )
                    "artistico" -> mostrarResultado(
                        "Perfil Creativo",
                        "Tienes una gran imaginación y sensibilidad estética. Podrías destacar en diseño, artes visuales, literatura o cine.",
                        R.drawable.vocacion2
                    )
                    "social" -> mostrarResultado(
                        "Perfil Humanitario",
                        "Te importan los demás y te motiva ayudar. Carreras como Psicología, Trabajo Social, Educación o Medicina podrían ser lo tuyo.",
                        R.drawable.vocacion3
                    )
                    "empresarial" -> mostrarResultado(
                        "Perfil Líder",
                        "Te gusta dirigir, emprender y asumir retos. Podrías encajar en Administración, Economía o Marketing.",
                        R.drawable.vocacion4
                    )
                    else -> throw IllegalArgumentException("Perfil vocacional desconocido: $perfilDominante")
                }
            }

        finalizarBtn.setOnClickListener {
            // Crear un Intent para redirigir a la actividad ContinuaVocacionalActivity
            val intent = Intent(this, ContinuaVocacionalActivity::class.java)
            startActivity(intent) // Iniciar la actividad
            finish() // Finalizar la actividad actual (opcional, si no quieres que el usuario regrese a esta pantalla al presionar el botón de retroceso)
        }
    }

    private fun mostrarResultado(titulo: String, desc: String, imagenRes: Int) {
        resultadoPrincipal.text = titulo
        descripcion.text = desc
        imagenResultado.setImageResource(imagenRes)
    }
}
