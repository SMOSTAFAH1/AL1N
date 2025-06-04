package com.grupo1.al1n.models;

import java.io.Serializable;

/**
 * Modelo para representar un elemento de criptomoneda
 * Implementa Serializable para poder pasar objetos entre Activities
 */
public class CryptoItem implements Serializable {
      // Propiedades básicas de la criptomoneda
    private String id;
    private String name;
    private String symbol;
    private double currentPrice;
    private double priceChange24h;
    private double priceChangePercentage24h;
    private String imageUrl;
    private long marketCap;
    private long totalVolume;
    private int rank;
    private boolean isFavorite;
      /**
     * Constructor completo
     */
    public CryptoItem(String id, String name, String symbol, double currentPrice, 
                     double priceChange24h, double priceChangePercentage24h, 
                     String imageUrl, long marketCap, long totalVolume) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.priceChange24h = priceChange24h;
        this.priceChangePercentage24h = priceChangePercentage24h;
        this.imageUrl = imageUrl;
        this.marketCap = marketCap;
        this.totalVolume = totalVolume;
        this.rank = 0;
        this.isFavorite = false;
    }
    
    /**
     * Constructor simplificado para datos de prueba
     */
    public CryptoItem(int rank, String symbol, String name, double currentPrice, 
                     double priceChangePercentage24h, double marketCap, 
                     double totalVolume, boolean isFavorite) {
        this.id = symbol.toLowerCase();
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.priceChange24h = 0.0;
        this.priceChangePercentage24h = priceChangePercentage24h;
        this.imageUrl = "";
        this.marketCap = (long) marketCap;
        this.totalVolume = (long) totalVolume;
        this.rank = rank;
        this.isFavorite = isFavorite;
    }
      /**
     * Constructor básico
     */
    public CryptoItem(String id, String name, String symbol, double currentPrice) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.priceChange24h = 0.0;
        this.priceChangePercentage24h = 0.0;
        this.imageUrl = "";
        this.marketCap = 0;
        this.totalVolume = 0;
        this.rank = 0;
        this.isFavorite = false;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public double getPriceChange24h() {
        return priceChange24h;
    }
    
    public void setPriceChange24h(double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }
    
    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }
    
    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public long getMarketCap() {
        return marketCap;
    }
    
    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }
    
    public long getTotalVolume() {
        return totalVolume;
    }
    
    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
    }
    
    /**
     * Obtiene el símbolo en mayúsculas
     */
    public String getSymbolUpperCase() {
        return symbol != null ? symbol.toUpperCase() : "";
    }
    
    /**
     * Obtiene el precio formateado como string
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", currentPrice);
    }
    
    /**
     * Obtiene el cambio de precio formateado como string
     */
    public String getFormattedPriceChange() {
        String sign = priceChangePercentage24h >= 0 ? "+" : "";
        return String.format("%s%.2f%%", sign, priceChangePercentage24h);
    }
    
    /**
     * Indica si el precio ha subido (cambio positivo)
     */
    public boolean isPriceUp() {
        return priceChangePercentage24h > 0;
    }
      /**
     * Indica si el precio ha bajado (cambio negativo)
     */
    public boolean isPriceDown() {
        return priceChangePercentage24h < 0;
    }
    
    /**
     * Getter para rank
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * Setter para rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }
    
    /**
     * Getter para isFavorite
     */
    public boolean isFavorite() {
        return isFavorite;
    }
    
    /**
     * Setter para isFavorite
     */
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
    
    /**
     * Alias para getCurrentPrice() para compatibilidad con el adapter
     */
    public double getPrice() {
        return currentPrice;
    }
    
    /**
     * Alias para getTotalVolume() para compatibilidad con el adapter
     */
    public double getVolume24h() {
        return totalVolume;
    }
    
    @Override
    public String toString() {
        return "CryptoItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", currentPrice=" + currentPrice +
                ", priceChangePercentage24h=" + priceChangePercentage24h +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CryptoItem that = (CryptoItem) obj;
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
