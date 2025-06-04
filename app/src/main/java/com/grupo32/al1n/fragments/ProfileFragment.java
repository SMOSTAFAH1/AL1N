package com.grupo32.al1n.fragments;

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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.grupo32.al1n.LoginActivity;
import com.grupo32.al1n.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Fragment para mostrar el perfil del usuario y configuraciones
 * Incluye funcionalidades de cambio de contraseña y cerrar sesión
 */
public class ProfileFragment extends Fragment {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_KEEP_SESSION = "keep_session";
    private static final String KEY_ACTIVE_USER = "active_user";
    private static final String KEY_USER_LIST = "user_list";
    
    // Canal de notificaciones
    private static final String CHANNEL_ID = "AL1N_Channel";
    private static final int NOTIFICATION_ID = 3;
    private static final int PASSWORD_CHANGE_NOTIFICATION_ID = 4;
    
    // Código de solicitud para permisos de notificación
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    // Componentes de la UI
    private TextView tvProfileTitle;
    private TextView tvUserInfo;
    private Button btnChangePassword;
    private Button btnLogout;
    private Button btnSwitchAccount;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    
    // Usuario activo
    private String activeUser;

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
        btnSwitchAccount = view.findViewById(R.id.btnSwitchAccount);
        
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        activeUser = sharedPreferences.getString(KEY_ACTIVE_USER, "");
        
        // Si no hay usuario activo y hay usuarios en la lista, establecer el primero como activo
        if (activeUser.isEmpty()) {
            Set<String> userList = sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>());
            if (!userList.isEmpty()) {
                activeUser = userList.iterator().next();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_ACTIVE_USER, activeUser);
                editor.apply();
            }
        }
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
                showChangePasswordDialog();
            }
        });

        // Listener del botón cerrar sesión
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
        
        // Listener del botón cambiar cuenta (si existe)
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
     * Configura la UI inicial del fragment
     */
    private void setupUI() {
        // Si no hay usuario activo, mostrar mensaje genérico
        if (activeUser.isEmpty()) {
            tvUserInfo.setText("Usuario: Invitado");
            return;
        }
        
        // Obtener nombre de usuario de SharedPreferences
        tvUserInfo.setText("Usuario: " + activeUser);
    }
    
    /**
     * Muestra el diálogo para cambiar de cuenta
     */
    private void showSwitchAccountDialog() {
        Set<String> userList = sharedPreferences.getStringSet(KEY_USER_LIST, new HashSet<String>());
        
        // Filtrar para no mostrar el usuario actual
        Set<String> otherUsers = new HashSet<>();
        for (String user : userList) {
            if (!user.equals(activeUser)) {
                otherUsers.add(user);
            }
        }
        
        if (otherUsers.isEmpty()) {
            Toast.makeText(requireContext(), "No hay otras cuentas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }
        
        final String[] users = otherUsers.toArray(new String[0]);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cambiar a cuenta")
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
        // Cerrar sesión del usuario actual
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getUserSpecificKey(KEY_IS_LOGGED_IN, activeUser), false);
        
        // Establecer nuevo usuario activo
        editor.putString(KEY_ACTIVE_USER, username);
        editor.putBoolean(getUserSpecificKey(KEY_IS_LOGGED_IN, username), true);
        editor.apply();
        
        // Mostrar mensaje de cambio y reiniciar la aplicación
        Toast.makeText(requireContext(), "Cambiando a cuenta: " + username, Toast.LENGTH_SHORT).show();
        
        // Volver a LoginActivity
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
    
    /**
     * Muestra el diálogo para cambiar contraseña
     */
    private void showChangePasswordDialog() {
        // Crear un LinearLayout para contener los campos de texto
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Crear los campos de texto para el diálogo
        final EditText etCurrentPassword = new EditText(requireContext());
        etCurrentPassword.setHint("Contraseña actual");
        etCurrentPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        // Configurar márgenes para los campos
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30); // Margen inferior de 30px
        etCurrentPassword.setLayoutParams(params);
        
        layout.addView(etCurrentPassword);
        
        final EditText etNewPassword = new EditText(requireContext());
        etNewPassword.setHint("Nueva contraseña");
        etNewPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPassword.setLayoutParams(params);
        layout.addView(etNewPassword);
        
        final EditText etConfirmPassword = new EditText(requireContext());
        etConfirmPassword.setHint("Confirmar nueva contraseña");
        etConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etConfirmPassword);

        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cambiar contraseña")
                .setView(layout)
                .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentPassword = etCurrentPassword.getText().toString().trim();
                        String newPassword = etNewPassword.getText().toString().trim();
                        String confirmPassword = etConfirmPassword.getText().toString().trim();
                        
                        changePassword(currentPassword, newPassword, confirmPassword);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    
    /**
     * Procesa el cambio de contraseña
     */
    private void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        // Validar que hay un usuario activo
        if (activeUser.isEmpty()) {
            Toast.makeText(requireContext(), "No hay usuario activo", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar campos vacíos
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Obtener contraseña almacenada
        String storedPassword = sharedPreferences.getString(getUserSpecificKey(KEY_PASSWORD, activeUser), "");
        
        // Si es la primera vez y no hay contraseña almacenada
        if (storedPassword.isEmpty()) {
            Toast.makeText(requireContext(), "No hay contraseña configurada. Configure una en el registro.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar contraseña actual
        if (!currentPassword.equals(storedPassword)) {
            Toast.makeText(requireContext(), "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar longitud mínima de la nueva contraseña
        if (newPassword.length() < 6) {
            Toast.makeText(requireContext(), "La nueva contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar que la nueva contraseña y la confirmación coincidan
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "La nueva contraseña y la confirmación no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar que la nueva contraseña sea diferente a la actual
        if (newPassword.equals(currentPassword)) {
            Toast.makeText(requireContext(), "La nueva contraseña debe ser diferente a la actual", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Guardar la nueva contraseña
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getUserSpecificKey(KEY_PASSWORD, activeUser), newPassword);
        editor.apply();
        
        // Mostrar mensaje de éxito
        Toast.makeText(requireContext(), "Contraseña modificada correctamente", Toast.LENGTH_SHORT).show();
        
        // Enviar notificación de cambio de contraseña
        sendPasswordChangeNotification();
    }
    
    /**
     * Genera una clave específica para cada usuario
     */
    private String getUserSpecificKey(String key, String username) {
        return username + "_" + key;
    }
    
    /**
     * Envía notificación de cambio de contraseña exitoso
     */
    private void sendPasswordChangeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("AL1N")
                .setContentText("Contraseña modificada correctamente")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(PASSWORD_CHANGE_NOTIFICATION_ID, builder.build());
    }

    /**
     * Realiza el proceso de logout
     */
    private void performLogout() {
        if (activeUser.isEmpty()) {
            Toast.makeText(requireContext(), "No hay usuario activo", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Limpiar SharedPreferences (marcar como no logueado y quitar mantener sesión)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getUserSpecificKey(KEY_IS_LOGGED_IN, activeUser), false);
        editor.putBoolean(getUserSpecificKey(KEY_KEEP_SESSION, activeUser), false);
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
