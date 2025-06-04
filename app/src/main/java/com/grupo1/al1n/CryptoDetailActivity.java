package com.grupo1.al1n;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

/**
 * Activity para mostrar los detalles de una criptomoneda
 * Incluye funcionalidad de compartir información
 */
public class CryptoDetailActivity extends AppCompatActivity {

    // Componentes de la UI
    private TextView tvSymbol, tvName, tvPrice, tvPriceChange;
    private TextView tvMarketCap, tvVolume, tvRank;
    private ImageButton btnShare, btnFavorite;
    private Toolbar toolbar;

    // Datos de la criptomoneda
    private String cryptoSymbol, cryptoName;
    private double cryptoPrice, cryptoChange, cryptoMarketCap, cryptoVolume;
    private int cryptoRank;
    private boolean isFavorite;

    // Formatter para números
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);

        // Inicializar formatter
        decimalFormat = new DecimalFormat("#,##0.00");

        // Obtener datos del intent
        getIntentData();

        // Inicializar componentes
        initializeComponents();

        // Configurar toolbar
        setupToolbar();

        // Configurar UI
        setupUI();

        // Configurar listeners
        setupListeners();
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
        isFavorite = false; // Por defecto, se implementará en Paso 4
    }

    /**
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
        btnFavorite = findViewById(R.id.btn_detail_favorite);
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
    }

    /**
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
        
        // Configurar botón de favorito
        int iconRes = isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        btnFavorite.setImageResource(iconRes);
    }

    /**
     * Configura los listeners de los botones
     */
    private void setupListeners() {
        btnShare.setOnClickListener(v -> shareCryptoInfo());
        btnFavorite.setOnClickListener(v -> toggleFavorite());
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
     * Alterna el estado de favorito
     */
    private void toggleFavorite() {
        isFavorite = !isFavorite;
        int iconRes = isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        btnFavorite.setImageResource(iconRes);
        
        String message = isFavorite ? "Added to favorites" : "Removed from favorites";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        
        // TODO: Implementar persistencia en Paso 4 con SQLite
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
