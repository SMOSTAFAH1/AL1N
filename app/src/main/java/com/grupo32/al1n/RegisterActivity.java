package com.grupo32.al1n;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ACTIVE_USER = "active_user";
    private static final String KEY_USER_LIST = "user_list";
    
    // Máximo de cuentas permitidas
    private static final int MAX_ACCOUNTS = 2;

    // Componentes de la UI
    private TextInputEditText etRegisterUsername, etRegisterPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar componentes
        initializeComponents();
        
        // Configurar listeners
        setupListeners();
    }

    /**
     * Inicializa los componentes de la UI y SharedPreferences
     */
    private void initializeComponents() {
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Configura los listeners de los botones
     */
    private void setupListeners() {
        // Listener del botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        // Listener para volver al login
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finalizar RegisterActivity
            }
        });
    }

    /**
     * Realiza el proceso de registro
     */
    private void performRegistration() {
        String username = etRegisterUsername.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        // Validar campos vacíos
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar longitud mínima de contraseña
        if (password.length() < 4) {
            Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Verificar si el usuario ya existe
        if (userExists(username)) {
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Guardar nuevo usuario
        if (saveNewUser(username, password)) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            // Volver al login
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Verifica si un usuario ya existe
     * @param username Usuario a verificar
     * @return true si el usuario existe
     */
    private boolean userExists(String username) {
        // Obtener lista de usuarios
        Set<String> userList = sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>());
        return userList.contains(username);
    }

    /**
     * Genera una clave específica para cada usuario
     */
    private String getUserSpecificKey(String key, String username) {
        return username + "_" + key;
    }

    /**
     * Guarda un nuevo usuario en SharedPreferences
     * @param username Usuario
     * @param password Contraseña
     * @return true si se guardó exitosamente
     */
    private boolean saveNewUser(String username, String password) {
        try {
            // Obtener lista de usuarios
            Set<String> userList = new HashSet<>(
                sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>()));
            
            // Verificar si se ha alcanzado el límite de cuentas
            if (userList.size() >= MAX_ACCOUNTS) {
                Toast.makeText(this, "Solo se permiten " + MAX_ACCOUNTS + " cuentas", Toast.LENGTH_SHORT).show();
                return false;
            }
            
            // Añadir nuevo usuario a la lista
            userList.add(username);
            
            SharedPreferences.Editor editor = sharedPreferences.edit();
            
            // Guardar lista actualizada de usuarios
            editor.putStringSet(KEY_USER_LIST, userList);
            
            // Guardar datos específicos del usuario
            editor.putString(getUserSpecificKey(KEY_USERNAME, username), username);
            editor.putString(getUserSpecificKey(KEY_PASSWORD, username), password);
            
            // Establecer como usuario activo si es el primer usuario
            if (userList.size() == 1) {
                editor.putString(KEY_ACTIVE_USER, username);
            }
            
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
