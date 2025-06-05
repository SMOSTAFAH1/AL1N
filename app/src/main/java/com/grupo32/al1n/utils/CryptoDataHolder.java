package com.grupo32.al1n.utils;

import com.grupo32.al1n.models.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase singleton para mantener los datos de cryptos disponibles
 * Permite compartir la información entre el HomeFragment y FavoritesFragment
 */
public class CryptoDataHolder {
    
    private static CryptoDataHolder instance;
    private List<Models.CryptoItem> allCryptos;
    
    private CryptoDataHolder() {
        allCryptos = new ArrayList<>();
    }
    
    /**
     * Obtiene la instancia singleton
     * @return Instancia de CryptoDataHolder
     */
    public static synchronized CryptoDataHolder getInstance() {
        if (instance == null) {
            instance = new CryptoDataHolder();
        }
        return instance;
    }
    
    /**
     * Actualiza la lista completa de cryptos
     * @param cryptos Lista de cryptos obtenidas del API
     */
    public void updateCryptos(List<Models.CryptoItem> cryptos) {
        allCryptos.clear();
        if (cryptos != null) {
            allCryptos.addAll(cryptos);
        }
    }
    
    /**
     * Obtiene todas las cryptos disponibles
     * @return Lista de todas las cryptos
     */
    public List<Models.CryptoItem> getAllCryptos() {
        return new ArrayList<>(allCryptos);
    }
    
    /**
     * Busca cryptos por nombre o símbolo
     * @param query Texto de búsqueda (nombre o símbolo)
     * @return Lista filtrada de cryptos que coinciden con la búsqueda
     */
    public List<Models.CryptoItem> searchCryptos(String query) {
        List<Models.CryptoItem> filteredList = new ArrayList<>();
        
        if (query == null || query.trim().isEmpty()) {
            return filteredList;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        
        for (Models.CryptoItem crypto : allCryptos) {
            // Buscar por nombre o símbolo
            if (crypto.getName().toLowerCase().contains(lowerQuery) ||
                crypto.getSymbol().toLowerCase().contains(lowerQuery)) {
                filteredList.add(crypto);
            }
        }
        
        return filteredList;
    }
    
    /**
     * Busca una crypto específica por símbolo exacto
     * @param symbol Símbolo de la crypto (ej: "BTC")
     * @return Models.CryptoItem si se encuentra, null en caso contrario
     */
    public Models.CryptoItem findCryptoBySymbol(String symbol) {
        if (symbol == null) return null;
        
        String upperSymbol = symbol.toUpperCase().trim();
        
        for (Models.CryptoItem crypto : allCryptos) {
            if (crypto.getSymbol().equalsIgnoreCase(upperSymbol)) {
                return crypto;
            }
        }
        
        return null;
    }
    
    /**
     * Indica si hay datos disponibles
     * @return true si hay cryptos cargadas, false en caso contrario
     */
    public boolean hasData() {
        return !allCryptos.isEmpty();
    }
    
    /**
     * Obtiene el número de cryptos disponibles
     * @return Número de cryptos en la lista
     */
    public int getCount() {
        return allCryptos.size();
    }
}
