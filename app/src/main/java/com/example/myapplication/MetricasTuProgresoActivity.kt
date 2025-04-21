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

    private lateinit var pieChart: PieChart
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuprogreso_metricas)

        pieChart = findViewById(R.id.pieChartVocacional)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Obtener los datos de Firestore
        firestore.collection("metricas_vocacionales")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Recuperar los valores de Firestore
                    val cientifico = document.getLong("cientifico")?.toInt() ?: 0
                    val artistico = document.getLong("artistico")?.toInt() ?: 0
                    val social = document.getLong("social")?.toInt() ?: 0
                    val empresarial = document.getLong("empresarial")?.toInt() ?: 0

                    // Crear la lista de entradas para la gráfica
                    val entries = mutableListOf<PieEntry>()
                    entries.add(PieEntry(cientifico.toFloat(), "Científico"))
                    entries.add(PieEntry(artistico.toFloat(), "Artístico"))
                    entries.add(PieEntry(social.toFloat(), "Social"))
                    entries.add(PieEntry(empresarial.toFloat(), "Empresarial"))

                    // Configurar el conjunto de datos del gráfico
                    val dataSet = PieDataSet(entries, "Perfil Vocacional")
                    dataSet.colors = listOf(
                        Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA
                    )

                    // Crear los datos del gráfico
                    val data = PieData(dataSet)
                    pieChart.data = data
                    pieChart.description.isEnabled = false
                    pieChart.invalidate() // Refrescar el gráfico para que se vea actualizado
                } else {
                    Toast.makeText(this, "No se encontraron datos de métricas.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el error si no se pueden recuperar los datos
                Toast.makeText(this, "Error al recuperar los datos: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}
