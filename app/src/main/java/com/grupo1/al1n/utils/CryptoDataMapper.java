package com.grupo1.al1n.utils;

import com.grupo1.al1n.models.CoinMarketCapResponse;
import com.grupo1.al1n.models.CryptoItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para convertir datos de CoinMarketCap a nuestro modelo CryptoItem
 */
public class CryptoDataMapper {
    
    /**
     * Convierte la respuesta de CoinMarketCap a una lista de CryptoItem
     * @param response Respuesta de la API de CoinMarketCap
     * @return Lista de objetos CryptoItem
     */
    public static List<CryptoItem> mapToCryptoItems(CoinMarketCapResponse response) {
        List<CryptoItem> cryptoItems = new ArrayList<>();
        
        if (response == null || response.getData() == null) {
            return cryptoItems;
        }
        
        for (CoinMarketCapResponse.CryptoData data : response.getData()) {
            try {
                // Obtener datos USD
                CoinMarketCapResponse.USD usdData = data.getQuote().getUsd();
                
                // Generar URL de imagen basada en el ID de CoinMarketCap
                String imageUrl = generateImageUrl(data.getId());
                
                // Crear objeto CryptoItem con todos los datos
                CryptoItem cryptoItem = new CryptoItem(
                    String.valueOf(data.getId()),           // id
                    data.getName(),                         // name
                    data.getSymbol(),                       // symbol
                    usdData.getPrice(),                     // currentPrice
                    calculatePriceChange24h(usdData),       // priceChange24h
                    usdData.getPercentChange24h(),          // priceChangePercentage24h
                    imageUrl,                               // imageUrl
                    (long) usdData.getMarketCap(),          // marketCap
                    (long) usdData.getVolume24h()           // totalVolume
                );
                
                // Establecer el ranking
                cryptoItem.setRank(data.getCmcRank());
                
                cryptoItems.add(cryptoItem);
                
            } catch (Exception e) {
                // Si hay algún error con una criptomoneda específica, continuar con las siguientes
                e.printStackTrace();
            }
        }
        
        return cryptoItems;
    }
    
    /**
     * Calcula el cambio de precio en 24h basado en el precio actual y el porcentaje
     * @param usdData Datos en USD de CoinMarketCap
     * @return Cambio de precio en 24h
     */
    private static double calculatePriceChange24h(CoinMarketCapResponse.USD usdData) {
        double currentPrice = usdData.getPrice();
        double percentChange = usdData.getPercentChange24h();
        
        // Calcular el precio anterior basado en el porcentaje de cambio
        double previousPrice = currentPrice / (1 + (percentChange / 100));
        
        // Retornar la diferencia
        return currentPrice - previousPrice;
    }
    
    /**
     * Genera la URL de la imagen de la criptomoneda
     * Utiliza el servicio de imágenes de CoinMarketCap
     * @param coinId ID de la criptomoneda en CoinMarketCap
     * @return URL de la imagen
     */
    private static String generateImageUrl(int coinId) {
        return "https://s2.coinmarketcap.com/static/img/coins/128x128/" + coinId + ".png";
    }
}
