package com.grupo1.al1n;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_KEEP_SESSION = "keep_session";
    
    // Canal de notificaciones
    private static final String CHANNEL_ID = "AL1N_Channel";
    private static final int NOTIFICATION_ID = 2;

    // Componentes de la UI
    private TextView tvWelcome;
    private Button btnLogout;

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar componentes
        initializeComponents();
        
        // Crear canal de notificaciones
        createNotificationChannel();
        
        // Configurar UI
        setupUI();
        
        // Configurar listeners
        setupListeners();
    }

    /**
     * Inicializa los componentes de la UI y SharedPreferences
     */
    private void initializeComponents() {
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Crea el canal de notificaciones para Android 8.0+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AL1N Notifications";
            String description = "Notificaciones de la aplicación AL1N";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Configura la UI con los datos del usuario
     */
    private void setupUI() {
        // Obtener nombre de usuario de SharedPreferences
        String username = sharedPreferences.getString(KEY_USERNAME, "Usuario");
        tvWelcome.setText("Bienvenido, " + username);
    }

    /**
     * Configura los listeners de los botones
     */
    private void setupListeners() {
        // Listener del botón de logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    /**
     * Realiza el proceso de logout
     */
    private void performLogout() {
        // Limpiar SharedPreferences (marcar como no logueado y quitar mantener sesión)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.putBoolean(KEY_KEEP_SESSION, false);
        editor.apply();
        
        // Enviar notificación de sesión cerrada
        sendLogoutNotification();
        
        // Volver a LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finalizar MainActivity
    }

    /**
     * Envía notificación de sesión cerrada
     */
    private void sendLogoutNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("AL1N")
                .setContentText("Sesión cerrada")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}