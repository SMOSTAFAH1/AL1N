package com.grupo32.al1n;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.Set;

import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_KEEP_SESSION = "keep_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_ACTIVE_USER = "active_user";
    private static final String KEY_USER_LIST = "user_list";
    
    // Máximo de cuentas permitidas
    private static final int MAX_ACCOUNTS = 2;
    
    // Canal de notificaciones
    private static final String CHANNEL_ID = "AL1N_Channel";
    private static final int NOTIFICATION_ID = 1;
    
    // Código de solicitud para permisos de notificación
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    // Componentes de la UI
    private TextInputEditText etUsername, etPassword;
    private CheckBox cbKeepSession;
    private Button btnLogin;
    private TextView tvGoToRegister;
    private Button btnSwitchAccount;

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
        
        // Verificar y solicitar permisos de notificación
        checkNotificationPermission();
        
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
        btnSwitchAccount = findViewById(R.id.btnSwitchAccount);
        
        // Verificar si el botón existe en el layout actual
        if (btnSwitchAccount == null) {
            // Si es necesario, añadirlo programáticamente
            // Por ahora, omitimos esta parte para no complicar el código
        }
        
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
     * Verifica si la app tiene permisos para mostrar notificaciones
     */
    private void checkNotificationPermission() {
        // Para Android 13 (API 33) y superior, es necesario solicitar el permiso POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, 
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar el permiso si no está concedido
                ActivityCompat.requestPermissions(this, 
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * Verifica si existe una sesión activa y redirige a MainActivity
     */
    private void checkExistingSession() {
        String activeUser = sharedPreferences.getString(KEY_ACTIVE_USER, "");
        
        if (!activeUser.isEmpty()) {
            boolean isLoggedIn = sharedPreferences.getBoolean(getUserSpecificKey(KEY_IS_LOGGED_IN, activeUser), false);
            boolean keepSession = sharedPreferences.getBoolean(getUserSpecificKey(KEY_KEEP_SESSION, activeUser), false);
            
            if (isLoggedIn && keepSession) {
                // Usuario tiene sesión activa, ir directamente a MainActivity
                startMainActivity();
            }
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
        
        // Listener para cambiar de cuenta (si el botón existe)
        if (btnSwitchAccount != null) {
            btnSwitchAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSwitchAccountDialog();
                }
            });
        }
    }
    
    /**
     * Muestra el diálogo para cambiar de cuenta
     */
    private void showSwitchAccountDialog() {
        Set<String> userList = sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>());
        
        if (userList.isEmpty()) {
            Toast.makeText(this, "No hay cuentas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }
        
        final String[] users = userList.toArray(new String[0]);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar cuenta")
                .setItems(users, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cambiar al usuario seleccionado
                        String selectedUser = users[which];
                        switchToUser(selectedUser);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    
    /**
     * Cambia al usuario seleccionado
     */
    private void switchToUser(String username) {
        // Verificar si el usuario seleccionado existe
        if (!sharedPreferences.contains(getUserSpecificKey(KEY_PASSWORD, username))) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Establecer el usuario activo
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACTIVE_USER, username);
        editor.apply();
        
        // Actualizar la UI
        etUsername.setText(username);
        etPassword.setText("");
        
        Toast.makeText(this, "Cambiado a cuenta: " + username, Toast.LENGTH_SHORT).show();
    }    /**
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
        
        // Verificar si hay usuarios registrados
        Set<String> userList = sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>());
        if (userList.isEmpty()) {
            Toast.makeText(this, "No hay cuentas registradas. Por favor, regístrate primero.", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Verificar credenciales
        if (validateCredentials(username, password)) {
            // Login exitoso
            saveLoginSession(username, password, cbKeepSession.isChecked());
            
            // Mostrar Snackbar de sesión iniciada
            View rootView = findViewById(android.R.id.content);
            Snackbar.make(rootView, "Sesión iniciada", Snackbar.LENGTH_LONG).show();
            
            sendLoginNotification();
            startMainActivity();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }/**
     * Valida las credenciales del usuario
     * @param username Usuario ingresado
     * @param password Contraseña ingresada
     * @return true si las credenciales son válidas
     */
    private boolean validateCredentials(String username, String password) {
        // Obtener lista de usuarios registrados
        Set<String> userList = sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>());
        
        // Si no hay usuarios registrados, no permitir login
        // El usuario debe registrarse primero
        if (userList.isEmpty()) {
            return false;
        }
        
        // Verificar si el usuario existe en la lista
        if (!userList.contains(username)) {
            return false;
        }
        
        // Obtener contraseña almacenada
        String storedPassword = sharedPreferences.getString(getUserSpecificKey(KEY_PASSWORD, username), "");
        
        // Verificar credenciales
        return password.equals(storedPassword);
    }

    /**
     * Guarda la sesión del usuario en SharedPreferences
     * @param username Usuario
     * @param password Contraseña
     * @param keepSession Si mantener la sesión
     */
    private void saveLoginSession(String username, String password, boolean keepSession) {
        // Obtener lista de usuarios
        Set<String> userList = new HashSet<>(
                sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>()));
        
        // Añadir usuario a la lista si no está presente
        if (!userList.contains(username)) {
            // Verificar si hay espacio para una nueva cuenta
            if (userList.size() >= MAX_ACCOUNTS) {
                Toast.makeText(this, "Solo se permiten " + MAX_ACCOUNTS + " cuentas", Toast.LENGTH_SHORT).show();
                return;
            }
            userList.add(username);
        }
        
        // Guardar lista actualizada de usuarios
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_USER_LIST, userList);
        
        // Guardar datos específicos del usuario
        editor.putString(getUserSpecificKey(KEY_USERNAME, username), username);
        editor.putString(getUserSpecificKey(KEY_PASSWORD, username), password);
        editor.putBoolean(getUserSpecificKey(KEY_KEEP_SESSION, username), keepSession);
        editor.putBoolean(getUserSpecificKey(KEY_IS_LOGGED_IN, username), true);
        
        // Establecer el usuario activo
        editor.putString(KEY_ACTIVE_USER, username);
        
        editor.apply();
    }
    
    /**
     * Genera una clave específica para cada usuario
     */
    private String getUserSpecificKey(String key, String username) {
        return username + "_" + key;
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
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, no necesitamos hacer nada ahora
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Las notificaciones están desactivadas", Toast.LENGTH_SHORT).show();
            }
        }
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
