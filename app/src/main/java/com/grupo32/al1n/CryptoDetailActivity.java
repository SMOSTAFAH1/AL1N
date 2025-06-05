package com.grupo32.al1n;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.grupo32.al1n.models.Models;
import com.grupo32.al1n.utils.ChartUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity para mostrar los detalles de una criptomoneda
 * Incluye funcionalidad de compartir información
 */
public class CryptoDetailActivity extends AppCompatActivity {    // Componentes de la UI
    private TextView tvSymbol, tvName, tvPrice, tvPriceChange;
    private TextView tvMarketCap, tvVolume, tvRank;
    private TextView tv24hHigh, tv24hLow, tv24hVolume;
    private TextView tvCirculatingSupply, tvTotalSupply, tvMaxSupply;
    private TextView tvAllTimeHigh, tvAthDate;
    private ImageButton btnShare;
    private Toolbar toolbar;
    
    // Componentes del gráfico
    private LineChart priceChart;
    private Button btn1d, btn7d, btn30d;

    // Datos de la criptomoneda
    private String cryptoSymbol, cryptoName;
    private double cryptoPrice, cryptoChange, cryptoMarketCap, cryptoVolume;
    private int cryptoRank;

    // Formatter para números
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);        // Inicializar formatter
        decimalFormat = new DecimalFormat("#,##0.00");
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        // Obtener datos del intent
        getIntentData();

        // Inicializar componentes
        initializeComponents();

        // Configurar toolbar
        setupToolbar();

        // Configurar UI
        setupUI();        // Configurar listeners
        setupListeners();
        
        // Configurar gráfico
        setupChart();
        
        // Cargar datos del gráfico
        loadChartData();
    }

    /**
     * Obtiene los datos de la criptomoneda del intent
     */
    private void getIntentData() {
        Intent intent = getIntent();
        cryptoSymbol = intent.getStringExtra("crypto_symbol");
        cryptoName = intent.getStringExtra("crypto_name");
        cryptoPrice = intent.getDoubleExtra("crypto_price", 0.0);
        cryptoChange = intent.getDoubleExtra("crypto_change", 0.0);
        cryptoMarketCap = intent.getDoubleExtra("crypto_market_cap", 0.0);
        cryptoVolume = intent.getDoubleExtra("crypto_volume", 0.0);
        cryptoRank = intent.getIntExtra("crypto_rank", 0);
    }    /**
     * Inicializa los componentes de la UI
     */
    private void initializeComponents() {
        toolbar = findViewById(R.id.toolbar);
        tvSymbol = findViewById(R.id.tv_detail_symbol);
        tvName = findViewById(R.id.tv_detail_name);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvPriceChange = findViewById(R.id.tv_detail_price_change);
        tvMarketCap = findViewById(R.id.tv_detail_market_cap);
        tvVolume = findViewById(R.id.tv_detail_volume);
        tvRank = findViewById(R.id.tv_detail_rank);
        btnShare = findViewById(R.id.btn_detail_share);
        
        // Componentes del gráfico
        priceChart = findViewById(R.id.price_chart);
        btn1d = findViewById(R.id.btn_1d);
        btn7d = findViewById(R.id.btn_7d);
        btn30d = findViewById(R.id.btn_30d);
        
        // Componentes adicionales de información
        tv24hHigh = findViewById(R.id.tv_24h_high);
        tv24hLow = findViewById(R.id.tv_24h_low);
        tv24hVolume = findViewById(R.id.tv_24h_volume);
        tvCirculatingSupply = findViewById(R.id.tv_circulating_supply);
        tvTotalSupply = findViewById(R.id.tv_total_supply);
        tvMaxSupply = findViewById(R.id.tv_max_supply);
        tvAllTimeHigh = findViewById(R.id.tv_all_time_high);
        tvAthDate = findViewById(R.id.tv_ath_date);
    }

    /**
     * Configura el toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(cryptoSymbol + " Details");
        }
    }    /**
     * Configura la UI con los datos de la criptomoneda
     */
    private void setupUI() {
        tvSymbol.setText(cryptoSymbol);
        tvName.setText(cryptoName);
        tvPrice.setText("$" + decimalFormat.format(cryptoPrice));
        
        // Configurar cambio de precio con color
        tvPriceChange.setText(String.format("%.2f%%", cryptoChange));
        int colorRes = cryptoChange >= 0 ? R.color.green : R.color.red;
        tvPriceChange.setTextColor(getResources().getColor(colorRes));
        
        tvMarketCap.setText("$" + formatLargeNumber(cryptoMarketCap));
        tvVolume.setText("$" + formatLargeNumber(cryptoVolume));
        tvRank.setText("#" + cryptoRank);
        
        // Configurar datos adicionales con valores de muestra
        setupAdditionalData();
    }

    /**
     * Configura datos adicionales de muestra para la pantalla
     */
    private void setupAdditionalData() {
        // Calcular valores de muestra basados en el precio actual
        double high24h = cryptoPrice * 1.05; // 5% más alto
        double low24h = cryptoPrice * 0.95;  // 5% más bajo
        
        tv24hHigh.setText("$" + decimalFormat.format(high24h));
        tv24hLow.setText("$" + decimalFormat.format(low24h));
        tv24hVolume.setText(formatLargeNumber(cryptoVolume / 1000000) + " " + cryptoSymbol);
        
        // Datos de suministro de muestra (Bitcoin como ejemplo)
        if (cryptoSymbol.equals("BTC")) {
            tvCirculatingSupply.setText("19.8M BTC");
            tvTotalSupply.setText("19.8M BTC");
            tvMaxSupply.setText("21M BTC");
            tvAllTimeHigh.setText("$69,045");
            tvAthDate.setText("Nov 10, 2021");
        } else {
            // Valores genéricos para otras criptomonedas
            double supply = cryptoMarketCap / cryptoPrice;
            tvCirculatingSupply.setText(formatLargeNumber(supply) + " " + cryptoSymbol);
            tvTotalSupply.setText(formatLargeNumber(supply * 1.1) + " " + cryptoSymbol);
            tvMaxSupply.setText(formatLargeNumber(supply * 1.5) + " " + cryptoSymbol);
            tvAllTimeHigh.setText("$" + decimalFormat.format(cryptoPrice * 2.5));
            tvAthDate.setText(dateFormat.format(new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000)));
        }
    }    /**
     * Configura los listeners de los botones
     */
    private void setupListeners() {
        btnShare.setOnClickListener(v -> shareCryptoInfo());
        
        // Listeners para botones de timeframe del gráfico
        btn1d.setOnClickListener(v -> loadChartData(1));
        btn7d.setOnClickListener(v -> loadChartData(7));
        btn30d.setOnClickListener(v -> loadChartData(30));
    }

    /**
     * Comparte la información de la criptomoneda
     */
    private void shareCryptoInfo() {
        String shareText = String.format(
            "%s (%s)\n" +
            "Price: $%s\n" +
            "24h Change: %.2f%%\n" +
            "Market Cap: $%s\n" +
            "Volume: $%s\n" +
            "Rank: #%d\n\n" +
            "Shared from AL1N Crypto App",
            cryptoName, cryptoSymbol,
            decimalFormat.format(cryptoPrice),
            cryptoChange,
            formatLargeNumber(cryptoMarketCap),
            formatLargeNumber(cryptoVolume),
            cryptoRank
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, cryptoSymbol + " - Crypto Info");

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share " + cryptoSymbol + " info"));
        }
    }

    /**
     * Configura el gráfico de precios
     */
    private void setupChart() {
        ChartUtils.setupLineChart(priceChart);
    }

    /**
     * Carga datos del gráfico con timeframe por defecto (7 días)
     */
    private void loadChartData() {
        loadChartData(7);
    }

    /**
     * Carga datos del gráfico para el timeframe especificado
     */
    private void loadChartData(int days) {
        // Actualizar estado visual de los botones
        updateTimeframeButtons(days);
        
        // Generar datos de muestra para el gráfico
        List<Models.PriceHistoryItem> priceHistory = ChartUtils.generateSampleData(cryptoPrice, days);
        
        // Actualizar gráfico
        ChartUtils.updateChartData(priceChart, priceHistory, cryptoChange >= 0);
    }

    /**
     * Actualiza el estado visual de los botones de timeframe
     */
    private void updateTimeframeButtons(int selectedDays) {
        // Resetear todos los botones
        btn1d.setBackgroundResource(R.drawable.circle_background);
        btn7d.setBackgroundResource(R.drawable.circle_background);
        btn30d.setBackgroundResource(R.drawable.circle_background);
        
        btn1d.setTextColor(getResources().getColor(R.color.black));
        btn7d.setTextColor(getResources().getColor(R.color.black));
        btn30d.setTextColor(getResources().getColor(R.color.black));
        
        // Destacar botón seleccionado
        Button selectedButton;
        switch (selectedDays) {
            case 1:
                selectedButton = btn1d;
                break;
            case 30:
                selectedButton = btn30d;
                break;
            case 7:
            default:
                selectedButton = btn7d;
                break;
        }
        
        selectedButton.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue));
        selectedButton.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Formatea números grandes (K, M, B)
     */
    private String formatLargeNumber(double number) {
        if (number >= 1_000_000_000) {
            return decimalFormat.format(number / 1_000_000_000) + "B";
        } else if (number >= 1_000_000) {
            return decimalFormat.format(number / 1_000_000) + "M";
        } else if (number >= 1_000) {
            return decimalFormat.format(number / 1_000) + "K";
        } else {
            return decimalFormat.format(number);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
