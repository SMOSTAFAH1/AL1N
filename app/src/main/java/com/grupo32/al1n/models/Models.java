package com.grupo32.al1n.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Archivo consolidado con todos los modelos de datos de la aplicación
 * Contiene solo las funciones esenciales que se usan en el código
 */
public class Models {

    // ==================== CRYPTOITEM ====================
    /**
     * Modelo para representar un elemento de criptomoneda
     */
    public static class CryptoItem implements Serializable {
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

        // Constructor completo
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

        // Constructor básico
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

        // Getters esenciales
        public String getId() { return id; }
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
        public double getCurrentPrice() { return currentPrice; }
        public double getPriceChange24h() { return priceChange24h; }
        public double getPriceChangePercentage24h() { return priceChangePercentage24h; }
        public String getImageUrl() { return imageUrl; }
        public long getMarketCap() { return marketCap; }
        public long getTotalVolume() { return totalVolume; }
        public int getRank() { return rank; }
        public boolean isFavorite() { return isFavorite; }

        // Setters esenciales
        public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
        public void setRank(int rank) { this.rank = rank; }
        public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

        // Métodos de utilidad
        public double getPrice() { return currentPrice; } // Alias para compatibilidad
        public double getVolume24h() { return totalVolume; } // Alias para compatibilidad
        public boolean isPriceUp() { return priceChangePercentage24h > 0; }

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

    // ==================== FAVORITEITEM ====================
    /**
     * Modelo para representar un favorito en la base de datos
     */
    public static class FavoriteItem implements Serializable {
        private long id;
        private String symbol;
        private String name;
        private double price;
        private String iconUrl;
        private boolean pinned;
        private long createdAt;        public FavoriteItem() {}

        // Constructor para nuevo favorito (usado por CryptoAdapter)
        public FavoriteItem(String symbol, String name, double price, String iconUrl, boolean pinned) {
            this.symbol = symbol;
            this.name = name;
            this.price = price;
            this.iconUrl = iconUrl;
            this.pinned = pinned;
            this.createdAt = System.currentTimeMillis() / 1000;
        }

        // Constructor completo para cargar desde base de datos (usado por FavoritesDao)
        public FavoriteItem(long id, String symbol, String name, double price, String iconUrl, boolean pinned, long createdAt) {
            this.id = id;
            this.symbol = symbol;
            this.name = name;
            this.price = price;
            this.iconUrl = iconUrl;
            this.pinned = pinned;
            this.createdAt = createdAt;
        }

        // Getters esenciales
        public long getId() { return id; }
        public String getSymbol() { return symbol; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getIconUrl() { return iconUrl; }
        public boolean isPinned() { return pinned; }
        public long getCreatedAt() { return createdAt; }

        // Setters esenciales
        public void setId(long id) { this.id = id; }
        public void setPinned(boolean pinned) { this.pinned = pinned; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            FavoriteItem that = (FavoriteItem) obj;
            return id == that.id || (symbol != null && symbol.equals(that.symbol));
        }

        @Override
        public int hashCode() {
            return symbol != null ? symbol.hashCode() : (int) id;
        }
    }

    // ==================== PRICEHISTORYITEM ====================
    /**
     * Modelo para representar un punto de datos históricos de precio
     */
    public static class PriceHistoryItem implements Serializable {
        private long timestamp;
        private double price;
        private double high;
        private double low;
        private double volume;

        public PriceHistoryItem() {}

        public PriceHistoryItem(long timestamp, double price, double high, double low, double volume) {
            this.timestamp = timestamp;
            this.price = price;
            this.high = high;
            this.low = low;
            this.volume = volume;
        }

        // Getters esenciales
        public long getTimestamp() { return timestamp; }
        public double getPrice() { return price; }
        public double getHigh() { return high; }
        public double getLow() { return low; }
        public double getVolume() { return volume; }
    }

    // ==================== COINMARKETCAPRESPONSE ====================
    /**
     * Modelo para la respuesta de la API de CoinMarketCap
     */
    public static class CoinMarketCapResponse {
        @SerializedName("data")
        private List<CryptoData> data;

        public List<CryptoData> getData() { return data; }

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
            @SerializedName("cmc_rank")
            private int cmcRank;
            @SerializedName("quote")
            private Quote quote;

            public int getId() { return id; }
            public String getName() { return name; }
            public String getSymbol() { return symbol; }
            public int getCmcRank() { return cmcRank; }
            public Quote getQuote() { return quote; }
        }

        /**
         * Clase interna para las cotizaciones
         */
        public static class Quote {
            @SerializedName("USD")
            private USD usd;

            public USD getUsd() { return usd; }
        }

        /**
         * Clase interna para los datos en USD
         */
        public static class USD {
            @SerializedName("price")
            private double price;
            @SerializedName("volume_24h")
            private double volume24h;
            @SerializedName("percent_change_24h")
            private double percentChange24h;
            @SerializedName("market_cap")
            private double marketCap;

            public double getPrice() { return price; }
            public double getVolume24h() { return volume24h; }
            public double getPercentChange24h() { return percentChange24h; }
            public double getMarketCap() { return marketCap; }
        }
    }
}
