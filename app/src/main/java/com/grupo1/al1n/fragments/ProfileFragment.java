package com.grupo1.al1n.fragments;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.grupo1.al1n.LoginActivity;
import com.grupo1.al1n.R;

/**
 * Fragment para mostrar el perfil del usuario y configuraciones
 * Incluye funcionalidades de cambio de contraseña y cerrar sesión
 */
public class ProfileFragment extends Fragment {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_KEEP_SESSION = "keep_session";
    
    // Canal de notificaciones
    private static final String CHANNEL_ID = "AL1N_Channel";
    private static final int NOTIFICATION_ID = 3;
    
    // Código de solicitud para permisos de notificación
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    // Componentes de la UI
    private TextView tvProfileTitle;
    private TextView tvUserInfo;
    private Button btnChangePassword;
    private Button btnLogout;

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    /**
     * Constructor vacío requerido
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Método factory para crear una nueva instancia del fragment
     * @return Nueva instancia de ProfileFragment
     */
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar componentes
        initializeComponents(view);
        
        // Crear canal de notificaciones
        createNotificationChannel();
        
        // Verificar y solicitar permisos de notificación
        checkNotificationPermission();
        
        // Configurar listeners
        setupListeners();
        
        // Configurar UI inicial
        setupUI();
    }

    /**
     * Inicializa los componentes de la UI y SharedPreferences
     * @param view Vista raíz del fragment
     */
    private void initializeComponents(View view) {
        tvProfileTitle = view.findViewById(R.id.tvProfileTitle);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnLogout = view.findViewById(R.id.btnLogout);
        
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
            
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    /**
     * Verifica si la app tiene permisos para mostrar notificaciones
     */
    private void checkNotificationPermission() {
        // Para Android 13 (API 33) y superior, es necesario solicitar el permiso POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), 
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar el permiso si no está concedido
                ActivityCompat.requestPermissions(requireActivity(), 
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * Configura los listeners de los botones
     */
    private void setupListeners() {
        // Listener del botón cambiar contraseña
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Por ahora solo mostramos un Toast
                // En el Paso 5 se implementará:
                // - AlertDialog con campos de contraseña
                // - Validación de contraseña actual
                // - Actualización de nueva contraseña
                // - Notificación de cambio exitoso
                Toast.makeText(getContext(), "Próximamente: Cambiar contraseña", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener del botón cerrar sesión
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    /**
     * Configura la UI inicial del fragment
     */
    private void setupUI() {
        // Obtener nombre de usuario de SharedPreferences
        String username = sharedPreferences.getString(KEY_USERNAME, "Usuario");
        tvUserInfo.setText("Usuario: " + username);
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
        
        // Mostrar un Snackbar para el cierre de sesión
        View rootView = getView();
        if (rootView != null) {
            Snackbar.make(rootView, "Sesión cerrada", Snackbar.LENGTH_LONG).show();
        } else {
            // Si no hay vista raíz, usar Toast como fallback
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_LONG).show();
        }
        
        // Enviar notificación de sesión cerrada
        sendLogoutNotification();
        
        // Volver a LoginActivity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Finalizar MainActivity
    }

    /**
     * Envía notificación de sesión cerrada
     */
    private void sendLogoutNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("AL1N")
                .setContentText("Sesión cerrada")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, no necesitamos hacer nada ahora
                Toast.makeText(requireContext(), "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado
                Toast.makeText(requireContext(), "Las notificaciones están desactivadas", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
