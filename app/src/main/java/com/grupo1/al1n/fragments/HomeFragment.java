package com.grupo1.al1n.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grupo1.al1n.R;

/**
 * Fragment para mostrar la lista de cryptos (Home)
 * En el Paso 3 se implementará la funcionalidad completa con RecyclerView y API
 */
public class HomeFragment extends Fragment {

    // Componentes de la UI
    private TextView tvHomeTitle;
    private TextView tvHomeContent;

    /**
     * Constructor vacío requerido
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Método factory para crear una nueva instancia del fragment
     * @return Nueva instancia de HomeFragment
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar componentes de la UI
        initializeComponents(view);
        
        // Configurar UI inicial
        setupUI();
    }

    /**
     * Inicializa los componentes de la UI
     * @param view Vista raíz del fragment
     */
    private void initializeComponents(View view) {
        tvHomeTitle = view.findViewById(R.id.tvHomeTitle);
        tvHomeContent = view.findViewById(R.id.tvHomeContent);
    }

    /**
     * Configura la UI inicial del fragment
     */
    private void setupUI() {
        // Por ahora solo mostramos el mensaje placeholder
        // En el Paso 3 se implementará:
        // - RecyclerView para mostrar cryptos
        // - Adapter para la lista
        // - Consulta a API para obtener precios
        // - Navegación a CryptoDetailActivity
    }
}
