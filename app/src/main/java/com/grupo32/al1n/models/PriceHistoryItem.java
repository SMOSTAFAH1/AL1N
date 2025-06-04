package com.grupo32.al1n.models;

import java.io.Serializable;

/**
 * Modelo para representar un punto de datos históricos de precio
 */
public class PriceHistoryItem implements Serializable {
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

    // Getters and Setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
