package com.grupo32.al1n.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente para la API de CoinMarketCap
 * Maneja la configuración de Retrofit y proporciona el servicio
 */
public class CoinMarketCapClient {
    
    // URL base de la API de CoinMarketCap
    private static final String BASE_URL = "https://pro-api.coinmarketcap.com/";
    
    // Tu API Key específica de CoinMarketCap
    public static final String API_KEY = "7a234b7e-5916-4cec-883c-7c1c2cf05217";
    
    // Instancia singleton del cliente Retrofit
    private static Retrofit retrofit = null;
    
    /**
     * Obtiene la instancia de Retrofit configurada
     * @return Instancia de Retrofit
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configurar logging para desarrollo (opcional)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Crear cliente OkHttp con interceptor de logging
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            
            // Crear instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
    
    /**
     * Obtiene el servicio de CoinMarketCap
     * @return Instancia del servicio CoinMarketCapService
     */
    public static CoinMarketCapService getService() {
        return getClient().create(CoinMarketCapService.class);
    }
}
