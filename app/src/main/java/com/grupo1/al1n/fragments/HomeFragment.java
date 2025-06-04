package com.grupo1.al1n.fragments;

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

import com.grupo1.al1n.R;
import com.grupo1.al1n.adapters.CryptoAdapter;
import com.grupo1.al1n.models.CryptoItem;
import com.grupo1.al1n.models.CoinMarketCapResponse;
import com.grupo1.al1n.services.CoinMarketCapClient;
import com.grupo1.al1n.services.CoinMarketCapService;
import com.grupo1.al1n.utils.CryptoDataMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment para mostrar la lista de cryptos (Home)
 * Implementado en el Paso 3 con RecyclerView y datos de CoinMarketCap API
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    
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
        
        // Cargar datos reales de CoinMarketCap
        loadCryptoDataFromAPI();
    }

    /**
     * Inicializa los componentes de la UI
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
    }    /**
     * Carga datos de criptomonedas desde la API de CoinMarketCap
     */
    private void loadCryptoDataFromAPI() {
        // Mostrar indicador de carga (opcional - podrías agregar un ProgressBar)
        Log.d(TAG, "Cargando datos de CoinMarketCap API...");
        
        // Realizar llamada a la API para obtener las top 20 criptomonedas
        Call<CoinMarketCapResponse> call = coinMarketCapService.getLatestListings(
            CoinMarketCapClient.API_KEY,
            1,      // start - comenzar desde la posición 1
            20,     // limit - obtener 20 criptomonedas
            "USD"   // convert - convertir a USD
        );
        
        call.enqueue(new Callback<CoinMarketCapResponse>() {
            @Override
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
                            
                            Log.d(TAG, "Lista actualizada con " + cryptoList.size() + " criptomonedas");
                        });
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta de la API: " + response.code());
                    handleApiError("Error al cargar datos: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<CoinMarketCapResponse> call, Throwable t) {
                Log.e(TAG, "Error en la llamada a la API", t);
                handleApiError("Error de conexión: " + t.getMessage());
                
                // Como fallback, cargar datos de ejemplo
                loadSampleCryptoData();
            }
        });
    }
    
    /**
     * Maneja errores de la API mostrando un mensaje al usuario
     * @param message Mensaje de error a mostrar
     */
    private void handleApiError(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            });
        }
    }    
    /**
     * Carga datos simulados de criptomonedas como respaldo
     * Se usa cuando la API no está disponible
     */
    private void loadSampleCryptoData() {
        Log.d(TAG, "Cargando datos de ejemplo como respaldo");
        
        cryptoList.clear();
        
        // Datos simulados de las principales criptomonedas
        cryptoList.add(new CryptoItem(
            1, "BTC", "Bitcoin", 67234.50, 2.45, 
            1324000000000.0, 28500000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            2, "ETH", "Ethereum", 2456.78, -1.23, 
            295600000000.0, 12300000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            3, "BNB", "BNB", 512.34, 0.87, 
            76400000000.0, 1200000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            4, "SOL", "Solana", 234.56, 4.12, 
            110200000000.0, 2800000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            5, "XRP", "XRP", 0.6234, -2.15, 
            35400000000.0, 1500000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            6, "DOGE", "Dogecoin", 0.3456, 1.78, 
            50800000000.0, 780000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            7, "ADA", "Cardano", 0.8912, -0.45, 
            31200000000.0, 450000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            8, "AVAX", "Avalanche", 34.67, 3.21, 
            13500000000.0, 340000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            9, "SHIB", "Shiba Inu", 0.000023, 5.67, 
            13600000000.0, 250000000.0, false
        ));
        
        cryptoList.add(new CryptoItem(
            10, "DOT", "Polkadot", 7.82, -1.89, 
            9800000000.0, 180000000.0, false
        ));
        
        // Notificar al adapter que los datos han cambiado
        cryptoAdapter.notifyDataSetChanged();
        
        Log.d(TAG, "Datos de ejemplo cargados: " + cryptoList.size() + " elementos");
    }
}
