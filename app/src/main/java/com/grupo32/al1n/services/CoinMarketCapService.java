package com.grupo32.al1n.services;

import com.grupo32.al1n.models.CoinMarketCapResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interfaz Retrofit para el servicio de CoinMarketCap API
 * Define los endpoints disponibles para consumir la API
 */
public interface CoinMarketCapService {
    
    /**
     * Obtiene las criptomonedas más populares
     * @param apiKey La API key de CoinMarketCap
     * @param start Posición de inicio (opcional, por defecto 1)
     * @param limit Número de resultados (opcional, por defecto 100, máximo 5000)
     * @param convert Moneda de conversión (opcional, por defecto USD)
     * @return Call con la respuesta de CoinMarketCap
     */
    @GET("v1/cryptocurrency/listings/latest")
    Call<CoinMarketCapResponse> getLatestListings(
        @Header("X-CMC_PRO_API_KEY") String apiKey,
        @Query("start") Integer start,
        @Query("limit") Integer limit,
        @Query("convert") String convert
    );
    
    /**
     * Obtiene las criptomonedas más populares con parámetros por defecto
     * @param apiKey La API key de CoinMarketCap
     * @return Call con la respuesta de CoinMarketCap (primeras 20 criptomonedas)
     */
    @GET("v1/cryptocurrency/listings/latest")
    Call<CoinMarketCapResponse> getTopCryptocurrencies(
        @Header("X-CMC_PRO_API_KEY") String apiKey
    );
}
