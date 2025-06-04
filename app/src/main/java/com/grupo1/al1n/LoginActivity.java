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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_KEEP_SESSION = "keep_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    // Canal de notificaciones
    private static final String CHANNEL_ID = "AL1N_Channel";
    private static final int NOTIFICATION_ID = 1;

    // Componentes de la UI
    private TextInputEditText etUsername, etPassword;
    private CheckBox cbKeepSession;
    private Button btnLogin;
    private TextView tvGoToRegister;

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar componentes
        initializeComponents();
        
        // Crear canal de notificaciones
        createNotificationChannel();
        
        // Verificar si el usuario ya tiene sesión iniciada
        checkExistingSession();
        
        // Configurar listeners
        setupListeners();
    }

    /**
     * Inicializa los componentes de la UI y SharedPreferences
     */
    private void initializeComponents() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbKeepSession = findViewById(R.id.cbKeepSession);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        
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
     * Verifica si existe una sesión activa y redirige a MainActivity
     */
    private void checkExistingSession() {
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        boolean keepSession = sharedPreferences.getBoolean(KEY_KEEP_SESSION, false);
        
        if (isLoggedIn && keepSession) {
            // Usuario tiene sesión activa, ir directamente a MainActivity
            startMainActivity();
        }
    }

    /**
     * Configura los listeners de los botones
     */
    private void setupListeners() {
        // Listener del botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // Listener para ir a RegisterActivity
        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Realiza el proceso de login
     */
    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validar campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Verificar credenciales (en este ejemplo, verificamos contra SharedPreferences)
        if (validateCredentials(username, password)) {
            // Login exitoso
            saveLoginSession(username, password, cbKeepSession.isChecked());
            sendLoginNotification();
            startMainActivity();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valida las credenciales del usuario
     * @param username Usuario ingresado
     * @param password Contraseña ingresada
     * @return true si las credenciales son válidas
     */
    private boolean validateCredentials(String username, String password) {
        // Obtener credenciales almacenadas
        String storedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String storedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
        
        // Si no hay usuario registrado, permitir cualquier login (demo)
        if (storedUsername.isEmpty()) {
            return true;
        }
        
        // Verificar credenciales
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    /**
     * Guarda la sesión del usuario en SharedPreferences
     * @param username Usuario
     * @param password Contraseña
     * @param keepSession Si mantener la sesión
     */
    private void saveLoginSession(String username, String password, boolean keepSession) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_KEEP_SESSION, keepSession);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    /**
     * Envía notificación de nuevo inicio de sesión
     */
    private void sendLoginNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("AL1N")
                .setContentText("Nuevo inicio de sesión")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Inicia MainActivity y finaliza LoginActivity
     */
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finalizar LoginActivity para que no se pueda volver con back button
    }
}
