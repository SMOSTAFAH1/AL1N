package com.grupo32.al1n.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para la respuesta de la API de CoinMarketCap
 * Solo incluye los campos que realmente se usan
 */
public class CoinMarketCapResponse {

    @SerializedName("data")
    private List<CryptoData> data;

    public List<CryptoData> getData() {
        return data;
    }

    public void setData(List<CryptoData> data) {
        this.data = data;
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

        @SerializedName("cmc_rank")
        private int cmcRank;

        @SerializedName("quote")
        private Quote quote;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        public int getCmcRank() {
            return cmcRank;
        }

        public Quote getQuote() {
            return quote;
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

        public double getPrice() {
            return price;
        }

        public double getVolume24h() {
            return volume24h;
        }

        public double getPercentChange24h() {
            return percentChange24h;
        }

        public double getMarketCap() {
            return marketCap;
        }
    }
}