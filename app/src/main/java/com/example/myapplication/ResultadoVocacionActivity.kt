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
                    "animales" to 0,
                    "diseño" to 0,
                    "finanzas" to 0,
                    "empresarial" to 0
                )


                for (doc in result) {
                    val respuesta = doc.getString("respuesta") ?: continue

                    when {
                        respuesta.contains("problemas", true) || respuesta.contains("ciencia", true) -> conteo["animales"] = conteo["animales"]!! + 1
                        respuesta.contains("arte", true) || respuesta.contains("creativo", true) || respuesta.contains("diseñado", true) -> conteo["diseño"] = conteo["diseño"]!! + 1
                        respuesta.contains("personas", true) || respuesta.contains("ayudar", true) -> conteo["finanzas"] = conteo["finanzas"]!! + 1
                        respuesta.contains("empresa", true) || respuesta.contains("liderado", true) -> conteo["empresarial"] = conteo["empresarial"]!! + 1
                    }
                }


                val perfilDominante = conteo.maxByOrNull { it.value }?.key

                when (perfilDominante) {
                    "animales" -> mostrarResultado(
                        "Perfil Minimalista",
                        "Eres una persona curiosa, lógica y resolutiva. Ideal para carreras como veterinaria, zootecnia, biologia marina y Biologia",
                        R.drawable.vocacion1
                    )
                    "diseño" -> mostrarResultado(
                        "Perfil De Diseño",
                        "Tienes una gran imaginación y sensibilidad estética. Podrías destacar en diseño grafico, arquitectura, Ingenieria Civil o Diseño Industrial.",
                        R.drawable.vocacion2
                    )
                    "finanzas" -> mostrarResultado(
                        "Perfil Financiero",
                        "Te importan los demás y te motiva ayudar. Carreras como Contabilidad, Economia, Banca o Negocios Internacionales podrían ser lo tuyo.",
                        R.drawable.vocacion3
                    )
                    "empresarial" -> mostrarResultado(
                        "Perfil Líder",
                        "Te gusta dirigir, emprender y asumir retos. Podrías encajar en Administración, finanzas, Economía o Marketing.",
                        R.drawable.vocacion4
                    )
                    else -> throw IllegalArgumentException("Perfil vocacional desconocido: $perfilDominante")
                }


                val intent = Intent(this, MetricasTuProgresoActivity::class.java).apply {
                    putExtra("animales", conteo["animales"] ?: 0)
                    putExtra("diseño", conteo["diseño"] ?: 0)
                    putExtra("finanzas", conteo["finanzas"] ?: 0)
                    putExtra("empresarial", conteo["empresarial"] ?: 0)
                }


                firestore.collection("metricas_vocacionales")
                    .document(userId)
                    .set(conteo)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Resultados guardados correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al guardar los resultados: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }


                finalizarBtn.setOnClickListener {

                    startActivity(intent)
                    finish()


                    val continuaIntent = Intent(this, ContinuaVocacionalActivity::class.java)
                    startActivity(continuaIntent)
                    finish()
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
