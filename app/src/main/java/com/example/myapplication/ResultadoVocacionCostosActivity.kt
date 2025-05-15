package com.example.myapplication

import android.content.Intent
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


                    when {
                        perfilVocacional == "finanzas" && perfilCostos == "presupuestoBajo" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosFinanzasEconomicas::class.java))
                        }
                        perfilVocacional == "finanzas" && perfilCostos == "moderado" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosFinanzasModerado::class.java))
                        }
                        perfilVocacional == "finanzas" && perfilCostos == "alternativaEconomica" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosFinanzasAltos::class.java))
                        }
                        perfilVocacional == "diseño" && perfilCostos == "presupuestoBajo" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosDisenoEconomicas::class.java))
                        }
                        perfilVocacional == "diseño" && perfilCostos == "moderado" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosDisenoModerado::class.java))
                        }
                        perfilVocacional == "diseño" && perfilCostos == "alternativaEconomica" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosDisenoAltos::class.java))
                        }
                        perfilVocacional == "animales" && perfilCostos == "moderado" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosAnimalesModeradoAlto::class.java))
                        }
                        perfilVocacional == "animales" && perfilCostos == "alternativaEconomica" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosAnimalesModeradoAlto::class.java))
                        }
                        perfilVocacional == "animales" && perfilCostos == "presupuestoBajo" -> {
                            startActivity(Intent(this, TuProgresoUniversidadesCostosAnimalesEconomicasActivity::class.java))
                        }
                        else -> {
                            startActivity(Intent(this, TuProgresoUniversidadescostosTodasCarreras::class.java))
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

