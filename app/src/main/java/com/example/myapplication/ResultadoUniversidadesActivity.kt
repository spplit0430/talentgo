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

class ResultadoUniversidadActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var resultadoPrincipal: TextView
    private lateinit var descripcion: TextView
    private lateinit var imagenResultado: ImageView
    private lateinit var finalizarBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultado_universidades)

        resultadoPrincipal = findViewById(R.id.resultado_principal)
        descripcion = findViewById(R.id.descripcion)
        imagenResultado = findViewById(R.id.imagen_resultado)
        finalizarBtn = findViewById(R.id.btn_finalizar)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid ?: return

        firestore.collection("respuestas_universidades")
            .document(userId)
            .collection("preguntas")
            .get()
            .addOnSuccessListener { result ->

                var prestigio = 0
                var costo = 0
                var publica = 0
                var privada = 0
                var online = 0
                var flexible = 0

                for (doc in result) {
                    val respuesta = doc.getString("respuesta") ?: continue

                    when (respuesta) {
                        "Colombia" -> publica++
                        "Estados Unidos o Europa", "En otro país de habla hispana" -> prestigio++
                        "No tengo preferencia" -> flexible++

                        "Pública" -> publica++
                        "Privada" -> privada++
                        "Online" -> online++

                        "Ciencias e ingeniería" -> publica++
                        "Artes y humanidades" -> privada++
                        "Negocios y economía" -> prestigio++
                        "Salud y medicina" -> publica++

                        "Prestigio y reconocimiento" -> prestigio++
                        "Costo de matrícula y becas" -> costo++
                        "Ubicación y cercanía" -> publica++
                        "Flexibilidad en horarios" -> flexible++
                    }
                }

                val conteo = mapOf(
                    "prestigio" to prestigio,
                    "costo" to costo,
                    "publica" to publica,
                    "privada" to privada,
                    "online" to online,
                    "flexible" to flexible
                )

                // Enviar los resultados al gráfico en la siguiente actividad
                val intent = Intent(this, MetricasTuProgresoActivity::class.java).apply {
                    putExtra("prestigio", prestigio)
                    putExtra("costo", costo)
                    putExtra("publica", publica)
                    putExtra("privada", privada)
                    putExtra("online", online)
                    putExtra("flexible", flexible)
                }

                // Guardar los resultados en Firestore
                firestore.collection("metricas_universidades")
                    .document(userId)
                    .set(conteo)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Resultados guardados correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al guardar los resultados: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }

                // Mostrar resultado visual en pantalla
                val perfil = when {
                    prestigio >= 2 && costo >= 2 -> "universidad_prestigio_economica"
                    publica >= 2 && online >= 2 -> "universidad_publica_tecnologica"
                    privada >= 2 && prestigio >= 2 -> "universidad_privada_prestigiosa"
                    online >= 2 && costo >= 2 -> "universidad_online_economica"
                    prestigio >= 2 -> "universidad_prestigiosa"
                    online >= 2 -> "universidad_online"
                    publica >= 2 -> "universidad_publica"
                    privada >= 2 -> "universidad_privada"
                    costo >= 2 -> "universidad_economica"
                    flexible >= 2 -> "universidad_flexible"
                    else -> "universidad_general"
                }

                mostrarResultadoPerfil(perfil)

                finalizarBtn.setOnClickListener {
                    startActivity(intent)  // Ir a Métricas
                    val continuaIntent = Intent(this, ContinuaUniversidadesActivity::class.java)
                    startActivity(continuaIntent)  // Ir a vista continua
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al obtener datos: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarResultadoPerfil(perfil: String) {
        when (perfil) {
            "universidad_prestigio_economica" -> mostrarResultado(
                "Universidad de Prestigio y Económica",
                "Buscas una universidad reconocida pero con buena relación costo-beneficio...",
                R.drawable.economica
            )
            "universidad_publica_tecnologica" -> mostrarResultado(
                "Universidad Pública y Tecnológica",
                "Prefieres universidades accesibles y modernas...",
                R.drawable.publico
            )
            "universidad_privada_prestigiosa" -> mostrarResultado(
                "Universidad Privada de Prestigio",
                "Valoras la reputación académica...",
                R.drawable.privada
            )
            "universidad_online_economica" -> mostrarResultado(
                "Universidad Online y Económica",
                "Buscas flexibilidad y economía...",
                R.drawable.online
            )
            "universidad_prestigiosa" -> mostrarResultado(
                "Universidad Prestigiosa",
                "Tu enfoque está en la calidad y reputación...",
                R.drawable.prestigio
            )
            "universidad_online" -> mostrarResultado(
                "Universidad Online",
                "Valoras estudiar desde casa...",
                R.drawable.online
            )
            "universidad_publica" -> mostrarResultado(
                "Universidad Pública",
                "Prefieres instituciones accesibles...",
                R.drawable.publico
            )
            "universidad_privada" -> mostrarResultado(
                "Universidad Privada",
                "Buscas una experiencia más personalizada...",
                R.drawable.privado
            )
            "universidad_economica" -> mostrarResultado(
                "Universidad Económica",
                "Tu prioridad es optimizar recursos...",
                R.drawable.economica
            )
            "universidad_flexible" -> mostrarResultado(
                "Universidad Flexible",
                "Buscas una institución que se adapte a tu estilo de vida...",
                R.drawable.universidad
            )
            else -> throw IllegalArgumentException("Perfil desconocido: $perfil")
        }
    }

    private fun mostrarResultado(titulo: String, desc: String, imagenRes: Int) {
        resultadoPrincipal.text = titulo
        descripcion.text = desc
        imagenResultado.setImageResource(imagenRes)
    }
}
