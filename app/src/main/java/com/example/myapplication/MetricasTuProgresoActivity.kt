package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
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
        firestore.collection("metricas_vocacionales")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val cientifico = document.getLong("cientifico")?.toInt() ?: 0
                    val artistico = document.getLong("artistico")?.toInt() ?: 0
                    val social = document.getLong("social")?.toInt() ?: 0
                    val empresarial = document.getLong("empresarial")?.toInt() ?: 0

                    val entriesVocacional = mutableListOf<PieEntry>()
                    entriesVocacional.add(PieEntry(cientifico.toFloat(), "Científico"))
                    entriesVocacional.add(PieEntry(artistico.toFloat(), "Artístico"))
                    entriesVocacional.add(PieEntry(social.toFloat(), "Social"))
                    entriesVocacional.add(PieEntry(empresarial.toFloat(), "Empresarial"))

                    val dataSetVocacional = PieDataSet(entriesVocacional, "PERFIL")
                    dataSetVocacional.colors = listOf(
                        Color.parseColor("#4CAF50"),  // Verde – Científico
                        Color.parseColor("#FF9800"),  // Naranja – Artístico
                        Color.parseColor("#2196F3"),  // Azul – Social
                        Color.parseColor("#9C27B0")   // Morado – Empresarial
                    )

                    val dataVocacional = PieData(dataSetVocacional)
                    dataVocacional.setValueTextSize(9f)
                    dataVocacional.setValueTextColor(Color.WHITE)

                    pieChartVocacional.data = dataVocacional
                    pieChartVocacional.description.isEnabled = false
                    pieChartVocacional.legend.textSize = 9f
                    pieChartVocacional.setEntryLabelColor(Color.BLACK)
                    pieChartVocacional.setEntryLabelTextSize(9f)
                    pieChartVocacional.invalidate()
                } else {
                    Toast.makeText(this, "No se encontraron datos de métricas vocacionales.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al recuperar los datos de vocación: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
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
                    val costo = document.getLong("costo")?.toInt() ?: 0
                    val flexible = document.getLong("flexible")?.toInt() ?: 0

                    // Crear las entradas para el gráfico de pastel (PieChart)
                    val entriesUniversidades = mutableListOf<PieEntry>()
                    entriesUniversidades.add(PieEntry(prestigio.toFloat(), "Prestigio"))
                    entriesUniversidades.add(PieEntry(publica.toFloat(), "Pública"))
                    entriesUniversidades.add(PieEntry(privada.toFloat(), "Privada"))
                    entriesUniversidades.add(PieEntry(online.toFloat(), "Online"))
                    entriesUniversidades.add(PieEntry(costo.toFloat(), "Costo"))
                    entriesUniversidades.add(PieEntry(flexible.toFloat(), "Flexible"))

                    // Configuración del gráfico de pastel
                    val dataSetUniversidades = PieDataSet(entriesUniversidades, "PERFIL")
                    dataSetUniversidades.colors = listOf(
                        Color.parseColor("#FF5722"),  // Naranja – Prestigio
                        Color.parseColor("#4CAF50"),  // Verde – Pública
                        Color.parseColor("#2196F3"),  // Azul – Privada
                        Color.parseColor("#9C27B0"),  // Morado – Online
                        Color.parseColor("#FFEB3B"),  // Amarillo – Costo
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
                    val becaImportante = document.getLong("becaImportante")?.toInt() ?: 0
                    val alternativaEconomica = document.getLong("alternativaEconomica")?.toInt() ?: 0
                    val flexible = document.getLong("flexible")?.toInt() ?: 0

                    val entriesCostos = mutableListOf<PieEntry>()
                    entriesCostos.add(PieEntry(presupuestoBajo.toFloat(), "Económico"))
                    entriesCostos.add(PieEntry(becaImportante.toFloat(), "Con Beca"))
                    entriesCostos.add(PieEntry(alternativaEconomica.toFloat(), "Alta Inversión"))
                    entriesCostos.add(PieEntry(flexible.toFloat(), "Flexible"))

                    val dataSetCostos = PieDataSet(entriesCostos, "PERFIL")
                    dataSetCostos.colors = listOf(
                        Color.parseColor("#43A047"),  // Verde – Económico
                        Color.parseColor("#FFC107"),  // Amarillo – Con Beca
                        Color.parseColor("#1E88E5"),  // Azul – Alta Inversión
                        Color.parseColor("#8E24AA")   // Morado – Flexible
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

