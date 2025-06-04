package com.grupo32.al1n.models;

import java.io.Serializable;

/**
 * Modelo para información detallada de criptomoneda con datos adicionales para gráficos
 */
public class CryptoDetailInfo implements Serializable {
    private String symbol;
    private String name;
    private double currentPrice;
    private double priceChangePercentage24h;
    private long marketCap;
    private long totalVolume;
    private int rank;
    
    // Datos adicionales para la pantalla de detalles
    private double high24h;
    private double low24h;
    private double circulatingSupply;
    private double totalSupply;
    private double maxSupply;
    private double allTimeHigh;
    private String athDate;
    private String imageUrl;
    
    public CryptoDetailInfo() {}

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(double high24h) {
        this.high24h = high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public void setLow24h(double low24h) {
        this.low24h = low24h;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public double getAllTimeHigh() {
        return allTimeHigh;
    }

    public void setAllTimeHigh(double allTimeHigh) {
        this.allTimeHigh = allTimeHigh;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Formatea el precio como string
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", currentPrice);
    }

    /**
     * Formatea el cambio de precio como string
     */
    public String getFormattedPriceChange() {
        String sign = priceChangePercentage24h >= 0 ? "+" : "";
        return String.format("%s%.2f%%", sign, priceChangePercentage24h);
    }

    /**
     * Indica si el precio ha subido
     */
    public boolean isPriceUp() {
        return priceChangePercentage24h > 0;
    }

    /**
     * Indica si el precio ha bajado
     */
    public boolean isPriceDown() {
        return priceChangePercentage24h < 0;
    }
}
