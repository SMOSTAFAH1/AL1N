package com.grupo32.al1n.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.grupo32.al1n.models.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Utilidad para configurar y actualizar gráficos de precio
 */
public class ChartUtils {

    /**
     * Configura el estilo básico del gráfico de líneas
     */
    public static void setupLineChart(LineChart chart) {
        // Configuración general del gráfico
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);

        // Configurar eje X (tiempo)
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(new DateValueFormatter());

        // Configurar eje Y izquierdo (precio)
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setValueFormatter(new PriceValueFormatter());

        // Deshabilitar eje Y derecho
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        // Configurar leyenda
        chart.getLegend().setEnabled(false);
    }

    /**
     * Actualiza el gráfico con nuevos datos de precio
     */
    public static void updateChartData(LineChart chart, List<Models.PriceHistoryItem> priceHistory, boolean isPriceUp) {
        ArrayList<Entry> entries = new ArrayList<>();

        // Convertir datos históricos a entradas del gráfico
        for (int i = 0; i < priceHistory.size(); i++) {
            Models.PriceHistoryItem item = priceHistory.get(i);
            entries.add(new Entry(item.getTimestamp(), (float) item.getPrice()));
        }

        // Crear dataset
        LineDataSet dataSet = new LineDataSet(entries, "Price");
        
        // Configurar estilo del dataset
        int lineColor = isPriceUp ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"); // Verde o Rojo
        dataSet.setColor(lineColor);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);

        // Configurar área bajo la curva con gradiente
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(lineColor);
        dataSet.setFillAlpha(30);

        // Aplicar datos al gráfico
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // Refrescar gráfico
    }

    /**
     * Genera datos de muestra para el gráfico (para pruebas)
     */
    public static List<Models.PriceHistoryItem> generateSampleData(double basePrice, int days) {
        List<Models.PriceHistoryItem> sampleData = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000L;

        for (int i = days; i >= 0; i--) {
            double priceVariation = (Math.random() - 0.5) * 0.1; // Variación del ±5%
            double price = basePrice * (1 + priceVariation);
            double high = price * (1 + Math.random() * 0.03); // High 3% mayor
            double low = price * (1 - Math.random() * 0.03);  // Low 3% menor
            double volume = 1000000 + Math.random() * 5000000; // Volumen aleatorio

            long timestamp = currentTime - (i * dayInMillis);

            sampleData.add(new Models.PriceHistoryItem(timestamp, price, high, low, volume));
            basePrice = price; // Para continuidad en el precio
        }

        return sampleData;
    }

    /**
     * Formateador para mostrar fechas en el eje X
     */
    private static class DateValueFormatter extends ValueFormatter {
        private SimpleDateFormat format = new SimpleDateFormat("MM/dd", Locale.getDefault());

        @Override
        public String getFormattedValue(float value) {
            return format.format(new Date((long) value));
        }
    }

    /**
     * Formateador para mostrar precios en el eje Y
     */
    private static class PriceValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            if (value >= 1000) {
                return String.format("$%.0fK", value / 1000);
            } else {
                return String.format("$%.0f", value);
            }
        }
    }
}
