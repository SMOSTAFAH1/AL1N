package com.grupo32.al1n.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo32.al1n.R;
import com.grupo32.al1n.adapters.CryptoAdapter;
import com.grupo32.al1n.models.CryptoItem;
import com.grupo32.al1n.models.CoinMarketCapResponse;
import com.grupo32.al1n.services.CoinMarketCapClient;
import com.grupo32.al1n.services.CoinMarketCapService;
import com.grupo32.al1n.utils.CryptoDataMapper;
import com.grupo32.al1n.utils.CryptoDataHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment para mostrar la lista de cryptos (Home)
 * Implementado en el Paso 3 con RecyclerView y datos de CoinMarketCap API
 */
public class HomeFragment extends Fragment {    private static final String TAG = "HomeFragment";
    
    // Componentes de la UI
    private RecyclerView recyclerView;
    private CryptoAdapter cryptoAdapter;
    private List<CryptoItem> cryptoList;

    // Servicio de CoinMarketCap
    private CoinMarketCapService coinMarketCapService;

    /**
     * Constructor vacío requerido
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Método factory para crear una nueva instancia del fragment
     * 
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
    }    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar servicio de CoinMarketCap
        coinMarketCapService = CoinMarketCapClient.getService();

        // Inicializar componentes de la UI
        initializeComponents(view);
        
        // Configurar RecyclerView
        setupRecyclerView();

        // Cargar datos de CoinMarketCap
        loadCryptoDataFromAPI();
    }

    /**
     * Inicializa los componentes de la UI
     * 
     * @param view Vista raíz del fragment
     */
    private void initializeComponents(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewCryptos);
    }

    /**
     * Configura el RecyclerView
     */
    private void setupRecyclerView() {
        cryptoList = new ArrayList<>();
        cryptoAdapter = new CryptoAdapter(getContext(), cryptoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cryptoAdapter);
    }

    /**
     * Carga datos de criptomonedas desde la API de CoinMarketCap
     */
    private void loadCryptoDataFromAPI() {
        // Mostrar indicador de carga (opcional - podrías agregar un ProgressBar)
        Log.d(TAG, "Cargando datos de CoinMarketCap API...");

        // Realizar llamada a la API para obtener las top 20 criptomonedas
        Call<CoinMarketCapResponse> call = coinMarketCapService.getLatestListings(
                CoinMarketCapClient.API_KEY,
                1, // start - comenzar desde la posición 1
                20, // limit - obtener 20 criptomonedas
                "USD" // convert - convertir a USD
        );

        call.enqueue(new Callback<CoinMarketCapResponse>() {            @Override
            public void onResponse(Call<CoinMarketCapResponse> call, Response<CoinMarketCapResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Datos recibidos exitosamente de CoinMarketCap");
                    
                    // Convertir respuesta a lista de CryptoItem
                    List<CryptoItem> apiCryptoList = CryptoDataMapper.mapToCryptoItems(response.body());

                    // Actualizar la lista en el hilo principal
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            cryptoList.clear();
                            cryptoList.addAll(apiCryptoList);
                            cryptoAdapter.notifyDataSetChanged();

                            // Guardar los datos en CryptoDataHolder para el autocompletado
                            CryptoDataHolder.getInstance().updateCryptos(apiCryptoList);

                            Log.d(TAG, "Lista actualizada con " + cryptoList.size() + " criptomonedas desde API");
                        });
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta de la API: " + response.code());
                    handleApiError("Error al cargar datos: " + response.code());
                }
            }            @Override
            public void onFailure(Call<CoinMarketCapResponse> call, Throwable t) {
                Log.e(TAG, "Error en la llamada a la API", t);
                handleApiError("Sin conexión a internet. No se pudieron cargar los datos.");
            }
        });
    }    /**
     * Maneja errores de la API mostrando un mensaje al usuario
     * 
     * @param message Mensaje de error a mostrar
     */
    private void handleApiError(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            });
        }
    }
}
