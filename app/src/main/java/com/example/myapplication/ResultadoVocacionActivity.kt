package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

                // Calcular las respuestas
                for (doc in result) {
                    val respuesta = doc.getString("respuesta") ?: continue

                    when {
                        respuesta.contains("problemas", true) || respuesta.contains("ciencia", true) -> conteo["cientifico"] = conteo["cientifico"]!! + 1
                        respuesta.contains("arte", true) || respuesta.contains("creativo", true) || respuesta.contains("diseñado", true) -> conteo["artistico"] = conteo["artistico"]!! + 1
                        respuesta.contains("personas", true) || respuesta.contains("ayudar", true) -> conteo["social"] = conteo["social"]!! + 1
                        respuesta.contains("empresa", true) || respuesta.contains("liderado", true) -> conteo["empresarial"] = conteo["empresarial"]!! + 1
                    }
                }

                // Mostrar los resultados vocacionales
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

                // Enviar los resultados al gráfico en la siguiente actividad
                val intent = Intent(this, MetricasTuProgresoActivity::class.java).apply {
                    putExtra("cientifico", conteo["cientifico"] ?: 0)
                    putExtra("artistico", conteo["artistico"] ?: 0)
                    putExtra("social", conteo["social"] ?: 0)
                    putExtra("empresarial", conteo["empresarial"] ?: 0)
                }

                // Guardar los resultados en Firestore para que se mantengan actualizados
                firestore.collection("metricas_vocacionales")
                    .document(userId)
                    .set(conteo)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Resultados guardados correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al guardar los resultados: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }

                // Configurar el botón para redirigir al siguiente Activity
                finalizarBtn.setOnClickListener {
                    // Redirigir a Métricas
                    startActivity(intent)  // Redirigir a la actividad de Métricas
                    finish()  // Finalizar la actividad actual

                    // También puedes redirigir a otra actividad si lo deseas
                    val continuaIntent = Intent(this, ContinuaVocacionalActivity::class.java)
                    startActivity(continuaIntent)  // Redirigir a la vista de continua_vocacion
                    finish()  // Finalizar esta actividad también
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al obtener datos: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarResultado(titulo: String, desc: String, imagenRes: Int) {
        resultadoPrincipal.text = titulo
        descripcion.text = desc
        imagenResultado.setImageResource(imagenRes)
    }
}
