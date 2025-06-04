package com.grupo32.al1n.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para la respuesta de la API de CoinMarketCap
 * Estructura JSON de la respuesta de /v1/cryptocurrency/listings/latest
 */
public class CoinMarketCapResponse {
    
    @SerializedName("status")
    private Status status;
    
    @SerializedName("data")
    private List<CryptoData> data;
    
    // Getters y setters
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public List<CryptoData> getData() {
        return data;
    }
    
    public void setData(List<CryptoData> data) {
        this.data = data;
    }
    
    /**
     * Clase interna para el estado de la respuesta
     */
    public static class Status {
        @SerializedName("timestamp")
        private String timestamp;
        
        @SerializedName("error_code")
        private int errorCode;
        
        @SerializedName("error_message")
        private String errorMessage;
        
        @SerializedName("elapsed")
        private int elapsed;
        
        @SerializedName("credit_count")
        private int creditCount;
        
        // Getters y setters
        public String getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        
        public int getErrorCode() {
            return errorCode;
        }
        
        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public int getElapsed() {
            return elapsed;
        }
        
        public void setElapsed(int elapsed) {
            this.elapsed = elapsed;
        }
        
        public int getCreditCount() {
            return creditCount;
        }
        
        public void setCreditCount(int creditCount) {
            this.creditCount = creditCount;
        }
    }
    
    /**
     * Clase interna para los datos de cada criptomoneda
     */
    public static class CryptoData {
        @SerializedName("id")
        private int id;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("symbol")
        private String symbol;
        
        @SerializedName("slug")
        private String slug;
        
        @SerializedName("cmc_rank")
        private int cmcRank;
        
        @SerializedName("num_market_pairs")
        private int numMarketPairs;
        
        @SerializedName("circulating_supply")
        private double circulatingSupply;
        
        @SerializedName("total_supply")
        private double totalSupply;
        
        @SerializedName("max_supply")
        private Double maxSupply;
        
        @SerializedName("last_updated")
        private String lastUpdated;
        
        @SerializedName("date_added")
        private String dateAdded;
        
        @SerializedName("tags")
        private List<String> tags;
        
        @SerializedName("platform")
        private Platform platform;
        
        @SerializedName("quote")
        private Quote quote;
        
        // Getters y setters
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
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
        
        public String getSlug() {
            return slug;
        }
        
        public void setSlug(String slug) {
            this.slug = slug;
        }
        
        public int getCmcRank() {
            return cmcRank;
        }
        
        public void setCmcRank(int cmcRank) {
            this.cmcRank = cmcRank;
        }
        
        public int getNumMarketPairs() {
            return numMarketPairs;
        }
        
        public void setNumMarketPairs(int numMarketPairs) {
            this.numMarketPairs = numMarketPairs;
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
        
        public Double getMaxSupply() {
            return maxSupply;
        }
        
        public void setMaxSupply(Double maxSupply) {
            this.maxSupply = maxSupply;
        }
        
        public String getLastUpdated() {
            return lastUpdated;
        }
        
        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
        
        public String getDateAdded() {
            return dateAdded;
        }
        
        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }
        
        public List<String> getTags() {
            return tags;
        }
        
        public void setTags(List<String> tags) {
            this.tags = tags;
        }
        
        public Platform getPlatform() {
            return platform;
        }
        
        public void setPlatform(Platform platform) {
            this.platform = platform;
        }
        
        public Quote getQuote() {
            return quote;
        }
        
        public void setQuote(Quote quote) {
            this.quote = quote;
        }
    }
    
    /**
     * Clase interna para la plataforma
     */
    public static class Platform {
        @SerializedName("id")
        private int id;
        
        @SerializedName("name")
        private String name;
        
        @SerializedName("symbol")
        private String symbol;
        
        @SerializedName("slug")
        private String slug;
        
        @SerializedName("token_address")
        private String tokenAddress;
        
        // Getters y setters
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
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
        
        public String getSlug() {
            return slug;
        }
        
        public void setSlug(String slug) {
            this.slug = slug;
        }
        
        public String getTokenAddress() {
            return tokenAddress;
        }
        
        public void setTokenAddress(String tokenAddress) {
            this.tokenAddress = tokenAddress;
        }
    }
    
    /**
     * Clase interna para las cotizaciones
     */
    public static class Quote {
        @SerializedName("USD")
        private USD usd;
        
        public USD getUsd() {
            return usd;
        }
        
        public void setUsd(USD usd) {
            this.usd = usd;
        }
    }
    
    /**
     * Clase interna para los datos en USD
     */
    public static class USD {
        @SerializedName("price")
        private double price;
        
        @SerializedName("volume_24h")
        private double volume24h;
        
        @SerializedName("volume_change_24h")
        private double volumeChange24h;
        
        @SerializedName("percent_change_1h")
        private double percentChange1h;
        
        @SerializedName("percent_change_24h")
        private double percentChange24h;
        
        @SerializedName("percent_change_7d")
        private double percentChange7d;
        
        @SerializedName("market_cap")
        private double marketCap;
        
        @SerializedName("market_cap_dominance")
        private double marketCapDominance;
        
        @SerializedName("fully_diluted_market_cap")
        private double fullyDilutedMarketCap;
        
        @SerializedName("last_updated")
        private String lastUpdated;
        
        // Getters y setters
        public double getPrice() {
            return price;
        }
        
        public void setPrice(double price) {
            this.price = price;
        }
        
        public double getVolume24h() {
            return volume24h;
        }
        
        public void setVolume24h(double volume24h) {
            this.volume24h = volume24h;
        }
        
        public double getVolumeChange24h() {
            return volumeChange24h;
        }
        
        public void setVolumeChange24h(double volumeChange24h) {
            this.volumeChange24h = volumeChange24h;
        }
        
        public double getPercentChange1h() {
            return percentChange1h;
        }
        
        public void setPercentChange1h(double percentChange1h) {
            this.percentChange1h = percentChange1h;
        }
        
        public double getPercentChange24h() {
            return percentChange24h;
        }
        
        public void setPercentChange24h(double percentChange24h) {
            this.percentChange24h = percentChange24h;
        }
        
        public double getPercentChange7d() {
            return percentChange7d;
        }
        
        public void setPercentChange7d(double percentChange7d) {
            this.percentChange7d = percentChange7d;
        }
        
        public double getMarketCap() {
            return marketCap;
        }
        
        public void setMarketCap(double marketCap) {
            this.marketCap = marketCap;
        }
        
        public double getMarketCapDominance() {
            return marketCapDominance;
        }
        
        public void setMarketCapDominance(double marketCapDominance) {
            this.marketCapDominance = marketCapDominance;
        }
        
        public double getFullyDilutedMarketCap() {
            return fullyDilutedMarketCap;
        }
        
        public void setFullyDilutedMarketCap(double fullyDilutedMarketCap) {
            this.fullyDilutedMarketCap = fullyDilutedMarketCap;
        }
        
        public String getLastUpdated() {
            return lastUpdated;
        }
        
        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
}
