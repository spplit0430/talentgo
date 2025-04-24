package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MetricasTuProgresoActivity : AppCompatActivity() {

    private lateinit var pieChartVocacional: PieChart
    private lateinit var pieChartUniversidades: PieChart
    private lateinit var pieChartCostos: PieChart
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuprogreso_metricas)

        pieChartVocacional = findViewById(R.id.pieChartVocacional)
        pieChartUniversidades = findViewById(R.id.pieChartUniversidades)
        pieChartCostos = findViewById(R.id.pieChartCostos)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Obtener las métricas vocacionales
        val btnVerPerfil = findViewById<Button>(R.id.btn_ver_perfil)
        val pieChartVocacional = findViewById<PieChart>(R.id.pieChartVocacional)

        firestore.collection("metricas_vocacionales")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val animales = document.getLong("animales")?.toInt() ?: 0
                    val diseno = document.getLong("diseño")?.toInt() ?: 0
                    val finanzas = document.getLong("finanzas")?.toInt() ?: 0
                    val empresarial = document.getLong("empresarial")?.toInt() ?: 0

                    // === Crear las entradas del gráfico tipo pastel ===
                    val entriesVocacion = mutableListOf<PieEntry>()
                    entriesVocacion.add(PieEntry(animales.toFloat(), "Animales"))
                    entriesVocacion.add(PieEntry(diseno.toFloat(), "Diseño"))
                    entriesVocacion.add(PieEntry(finanzas.toFloat(), "Finanzas"))
                    entriesVocacion.add(PieEntry(empresarial.toFloat(), "Empresarial"))

                    val dataSetVocacion = PieDataSet(entriesVocacion, "VOCACION")
                    dataSetVocacion.colors = listOf(
                        Color.parseColor("#FFC107"), // Ámbar – Animales
                        Color.parseColor("#03A9F4"), // Celeste – Diseño
                        Color.parseColor("#4CAF50"), // Verde – Finanzas
                        Color.parseColor("#9C27B0")  // Morado – Empresarial
                    )

                    val dataVocacion = PieData(dataSetVocacion)
                    dataVocacion.setValueTextSize(9f)
                    dataVocacion.setValueTextColor(Color.WHITE)

                    // Configurar el gráfico
                    pieChartVocacional.data = dataVocacion
                    pieChartVocacional.description.isEnabled = false
                    pieChartVocacional.legend.textSize = 9f
                    pieChartVocacional.setEntryLabelColor(Color.BLACK)
                    pieChartVocacional.setEntryLabelTextSize(9f)
                    pieChartVocacional.invalidate()

                    // === Determinar perfil con mayor puntaje ===
                    val puntajes = mapOf(
                        "animales" to animales,
                        "diseño" to diseno,
                        "finanzas" to finanzas,
                        "empresarial" to empresarial
                    )
                    val perfilGanador = puntajes.maxByOrNull { it.value }?.key

                    btnVerPerfil.setOnClickListener {
                        when (perfilGanador) {
                            "animales" -> startActivity(Intent(this, TuProgresoVocacionAnimalesActivity::class.java))
                            "diseño" -> startActivity(Intent(this, TuProgresoVocacionDisenoActivity::class.java))
                            "finanzas" -> startActivity(Intent(this, TuProgresoVocacionFinanzasActivity::class.java))
                            "empresarial" -> startActivity(Intent(this, TuProgresoVocacionFinanzasActivity::class.java))
                            else -> Toast.makeText(this, "No se pudo determinar el perfil más alto.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val btnVerCombinado = findViewById<Button>(R.id.btn_ver_combinado)
                    btnVerCombinado.setOnClickListener {
                        val intent = Intent(this, ResultadoVocacionCostosActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    Toast.makeText(this, "No se encontraron datos de métricas vocacionales.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al recuperar los datos: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        // Obtener las métricas de universidades
        firestore.collection("metricas_universidades")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Recuperar las métricas para universidades
                    val prestigio = document.getLong("prestigio")?.toInt() ?: 0
                    val publica = document.getLong("publica")?.toInt() ?: 0
                    val privada = document.getLong("privada")?.toInt() ?: 0
                    val online = document.getLong("online")?.toInt() ?: 0
                    val flexible = document.getLong("flexible")?.toInt() ?: 0

                    // Crear las entradas para el gráfico de pastel (PieChart)
                    val entriesUniversidades = mutableListOf<PieEntry>()
                    entriesUniversidades.add(PieEntry(prestigio.toFloat(), "Prestigio"))
                    entriesUniversidades.add(PieEntry(publica.toFloat(), "Pública"))
                    entriesUniversidades.add(PieEntry(privada.toFloat(), "Privada"))
                    entriesUniversidades.add(PieEntry(online.toFloat(), "Online"))
                    entriesUniversidades.add(PieEntry(flexible.toFloat(), "Flexible"))

                    // Configuración del gráfico de pastel
                    val dataSetUniversidades = PieDataSet(entriesUniversidades, "UNIVERSIDADES")
                    dataSetUniversidades.colors = listOf(
                        Color.parseColor("#FF5722"),  // Naranja – Prestigio
                        Color.parseColor("#4CAF50"),  // Verde – Pública
                        Color.parseColor("#2196F3"),  // Azul – Privada
                        Color.parseColor("#9C27B0"),  // Morado – Online
                        Color.parseColor("#8BC34A")   // Verde Claro – Flexible
                    )

                    val dataUniversidades = PieData(dataSetUniversidades)
                    dataUniversidades.setValueTextSize(9f)
                    dataUniversidades.setValueTextColor(Color.WHITE)

                    // Asignar los datos al gráfico
                    pieChartUniversidades.data = dataUniversidades
                    pieChartUniversidades.description.isEnabled = false
                    pieChartUniversidades.legend.textSize = 9f
                    pieChartUniversidades.setEntryLabelColor(Color.BLACK)
                    pieChartUniversidades.setEntryLabelTextSize(9f)
                    pieChartUniversidades.invalidate()
                } else {
                    Toast.makeText(this, "No se encontraron datos de métricas de universidades.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al recuperar los datos de universidades: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        // MÉTRICAS COSTOS
        firestore.collection("metricas_costos")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val presupuestoBajo = document.getLong("presupuestoBajo")?.toInt() ?: 0
                    val alternativaEconomica = document.getLong("alternativaEconomica")?.toInt() ?: 0
                    val moderado = document.getLong("moderado")?.toInt() ?: 0

                    val entriesCostos = mutableListOf<PieEntry>()
                    entriesCostos.add(PieEntry(presupuestoBajo.toFloat(), "Económico"))
                    entriesCostos.add(PieEntry(alternativaEconomica.toFloat(), "Alta Inversión"))
                    entriesCostos.add(PieEntry(moderado.toFloat(), "Moderado"))

                    val dataSetCostos = PieDataSet(entriesCostos, "COSTOS")
                    dataSetCostos.colors = listOf(
                        Color.parseColor("#43A047"),  // Verde – Económico
                        Color.parseColor("#1E88E5"),  // Azul – Alta Inversión
                        Color.parseColor("#8E24AA")   // Morado – Moderado
                    )

                    val dataCostos = PieData(dataSetCostos)
                    dataCostos.setValueTextSize(9f)
                    dataCostos.setValueTextColor(Color.WHITE)

                    pieChartCostos.data = dataCostos
                    pieChartCostos.description.isEnabled = false
                    pieChartCostos.legend.textSize = 9f
                    pieChartCostos.setEntryLabelColor(Color.BLACK)
                    pieChartCostos.setEntryLabelTextSize(9f)
                    pieChartCostos.invalidate()
                } else {
                    Toast.makeText(this, "No se encontraron datos de métricas de costos.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al recuperar los datos de costos: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}

