package com.grupo1.al1n;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grupo1.al1n.fragments.FavoritesFragment;
import com.grupo1.al1n.fragments.HomeFragment;
import com.grupo1.al1n.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_USERNAME = "username";

    // Componentes de la UI
    private BottomNavigationView bottomNavigationView;

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    // Fragments
    private HomeFragment homeFragment;
    private FavoritesFragment favoritesFragment;
    private ProfileFragment profileFragment;

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
        
        // Inicializar fragments
        initializeFragments();
        
        // Configurar navegación
        setupBottomNavigation();
        
        // Mostrar fragment inicial (Home)
        if (savedInstanceState == null) {
            showFragment(homeFragment);
        }
    }

    /**
     * Inicializa los componentes de la UI y SharedPreferences
     */
    private void initializeComponents() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Inicializa los fragments que se usarán en la navegación
     */
    private void initializeFragments() {
        homeFragment = HomeFragment.newInstance();
        favoritesFragment = FavoritesFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
    }

    /**
     * Configura la navegación del BottomNavigationView
     */
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = homeFragment;
            } else if (itemId == R.id.nav_favorites) {
                selectedFragment = favoritesFragment;
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = profileFragment;
            }
            
            if (selectedFragment != null) {
                showFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    /**
     * Muestra el fragment seleccionado en el contenedor
     * @param fragment Fragment a mostrar
     */
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Reemplazar el fragment actual
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}