package com.grupo1.al1n.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grupo1.al1n.R;

/**
 * Fragment para mostrar y gestionar cryptos favoritos
 * En el Paso 4 se implementará la funcionalidad completa con SQLite y RecyclerView
 */
public class FavoritesFragment extends Fragment {

    // Componentes de la UI
    private TextView tvFavoritesTitle;
    private TextView tvFavoritesContent;
    private FloatingActionButton fabAddFavorite;

    /**
     * Constructor vacío requerido
     */
    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Método factory para crear una nueva instancia del fragment
     * @return Nueva instancia de FavoritesFragment
     */
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar componentes de la UI
        initializeComponents(view);
        
        // Configurar listeners
        setupListeners();
        
        // Configurar UI inicial
        setupUI();
    }

    /**
     * Inicializa los componentes de la UI
     * @param view Vista raíz del fragment
     */
    private void initializeComponents(View view) {
        tvFavoritesTitle = view.findViewById(R.id.tvFavoritesTitle);
        tvFavoritesContent = view.findViewById(R.id.tvFavoritesContent);
        fabAddFavorite = view.findViewById(R.id.fabAddFavorite);
    }

    /**
     * Configura los listeners de los componentes
     */
    private void setupListeners() {
        // Listener del FloatingActionButton
        fabAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Por ahora solo mostramos un Toast
                // En el Paso 4 se implementará:
                // - AlertDialog para agregar crypto
                // - Validación de entrada
                // - Inserción en base de datos SQLite
                Toast.makeText(getContext(), "Próximamente: Agregar crypto favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Configura la UI inicial del fragment
     */
    private void setupUI() {
        // Por ahora solo mostramos el mensaje placeholder
        // En el Paso 4 se implementará:
        // - Base de datos SQLite (FavoritesDbHelper)
        // - RecyclerView con adapter personalizado
        // - ItemTouchHelper para swipe gestures
        // - Funcionalidad de pinear/despinear
        // - Eliminación por swipe
    }
}
