package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResultadoVocacionCostosActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        obtenerPerfilCombinado()
    }

    private fun obtenerPerfilCombinado() {
        val vocacionRef = firestore.collection("metricas_vocacionales").document(userId!!)
        val costosRef = firestore.collection("metricas_costos").document(userId)

        vocacionRef.get().addOnSuccessListener { vocDoc ->
            costosRef.get().addOnSuccessListener { costoDoc ->

                if (vocDoc.exists() && costoDoc.exists()) {
                    val perfilVocacional = vocDoc.data?.maxByOrNull { it.value as Long }?.key
                    val perfilCostos = costoDoc.data?.maxByOrNull { it.value as Long }?.key

                    // === Aquí decides qué layout mostrar según la combinación ===
                    when {
                        perfilVocacional == "finanzas" && perfilCostos == "presupuestoBajo" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_finanzas_economicas)
                        }
                        perfilVocacional == "finanzas" && perfilCostos == "moderado" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_finanzas_moderado)
                        }
                        perfilVocacional == "finanzas" && perfilCostos == "alternativaEconomica" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_finanzas_altos)
                        }
                        perfilVocacional == "diseño" && perfilCostos == "presupuestoBajo" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_diseno_economicas)
                        }
                        perfilVocacional == "diseño" && perfilCostos == "moderado" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_diseno_moderado)
                        }
                        perfilVocacional == "diseño" && perfilCostos == "alternativaEconomica" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_diseno_altos)
                        }
                        perfilVocacional == "animales" && perfilCostos == "moderado" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_animales_moderadoalto)
                        }
                        perfilVocacional == "animales" && perfilCostos == "presupuestoBajo" -> {
                            setContentView(R.layout.tuprogreso_universidadescostos_animales_economicas)
                        }
                        else -> {
                            // Manejo por defecto si no se encuentra una combinación válida
                            setContentView(R.layout.activity_agendamiento)
                        }
                    }

                } else {
                    Toast.makeText(this, "Datos incompletos para mostrar resultados", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Error al leer datos de costos", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Error al leer datos vocacionales", Toast.LENGTH_SHORT).show()
        }
    }
}

