package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
                        // Pregunta 1
                        "Colombia" -> publica++
                        "Estados Unidos o Europa" -> prestigio++
                        "En otro país de habla hispana" -> prestigio++
                        "No tengo preferencia" -> flexible++

                        // Pregunta 2
                        "Pública" -> publica++
                        "Privada" -> privada++
                        "Online" -> online++
                        "No tengo preferencia" -> flexible++

                        // Pregunta 3
                        "Ciencias e ingeniería" -> publica++ // o prestigio++, según lo que quieras destacar
                        "Artes y humanidades" -> privada++
                        "Negocios y economía" -> prestigio++
                        "Salud y medicina" -> publica++

                        // Pregunta 4
                        "Prestigio y reconocimiento" -> prestigio++
                        "Costo de matrícula y becas" -> costo++
                        "Ubicación y cercanía" -> publica++
                        "Flexibilidad en horarios" -> flexible++
                    }
                }

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
            }

        finalizarBtn.setOnClickListener {
            startActivity(Intent(this, ContinuaUniversidadesActivity::class.java))
            finish()
        }
    }

    private fun mostrarResultadoPerfil(perfil: String) {
        when (perfil) {
            "universidad_prestigio_economica" -> mostrarResultado(
                "Universidad de Prestigio y Económica",
                "Buscas una universidad reconocida pero con buena relación costo-beneficio. Podrías considerar universidades públicas de alto nivel o con buenas opciones de becas.",
                R.drawable.economica
            )
            "universidad_publica_tecnologica" -> mostrarResultado(
                "Universidad Pública y Tecnológica",
                "Prefieres universidades accesibles y modernas, con opciones online o presenciales. Las universidades tecnológicas públicas podrían ser ideales para ti.",
                R.drawable.publico
            )
            "universidad_privada_prestigiosa" -> mostrarResultado(
                "Universidad Privada de Prestigio",
                "Valoras la reputación académica y estás dispuesto a invertir en tu educación. Podrías destacar en instituciones privadas reconocidas internacionalmente.",
                R.drawable.privada
            )
            "universidad_online_economica" -> mostrarResultado(
                "Universidad Online y Económica",
                "Buscas flexibilidad y economía. Las universidades online con buena oferta académica pueden ser una gran opción.",
                R.drawable.online
            )
            "universidad_prestigiosa" -> mostrarResultado(
                "Universidad Prestigiosa",
                "Tu enfoque está en la calidad y reputación. Apunta a las universidades mejor posicionadas en rankings internacionales.",
                R.drawable.prestigio
            )
            "universidad_online" -> mostrarResultado(
                "Universidad Online",
                "Valoras estudiar desde casa, administrar tu tiempo y avanzar a tu ritmo. Las universidades virtuales son para ti.",
                R.drawable.online
            )
            "universidad_publica" -> mostrarResultado(
                "Universidad Pública",
                "Prefieres instituciones accesibles y con gran diversidad estudiantil. Las universidades públicas pueden brindarte una excelente formación.",
                R.drawable.publico
            )
            "universidad_privada" -> mostrarResultado(
                "Universidad Privada",
                "Buscas una experiencia más personalizada y estás dispuesto a invertir. Las universidades privadas te ofrecen muchas alternativas.",
                R.drawable.privado
            )
            "universidad_economica" -> mostrarResultado(
                "Universidad Económica",
                "Tu prioridad es optimizar recursos. Existen muchas opciones con bajo costo y alta calidad.",
                R.drawable.economica
            )
            "universidad_flexible" -> mostrarResultado(
                "Universidad Flexible",
                "Buscas una institución que se adapte a tu estilo de vida. Las opciones con horarios flexibles u online te pueden beneficiar.",
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
