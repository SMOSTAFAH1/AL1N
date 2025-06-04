package com.grupo1.al1n.models;

import java.io.Serializable;

/**
 * Modelo para representar un favorito en la base de datos
 * Estructura simplificada para la funcionalidad de favoritos
 */
public class FavoriteItem implements Serializable {
    
    private long id;
    private String name;
    private double price;
    private String iconUrl;
    private boolean pinned;
    private long createdAt;
    
    /**
     * Constructor vacío
     */
    public FavoriteItem() {
    }
    
    /**
     * Constructor completo
     */
    public FavoriteItem(long id, String name, double price, String iconUrl, boolean pinned, long createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.iconUrl = iconUrl;
        this.pinned = pinned;
        this.createdAt = createdAt;
    }
    
    /**
     * Constructor para nuevo favorito (sin ID)
     */
    public FavoriteItem(String name, double price, String iconUrl, boolean pinned) {
        this.name = name;
        this.price = price;
        this.iconUrl = iconUrl;
        this.pinned = pinned;
        this.createdAt = System.currentTimeMillis() / 1000; // Unix timestamp
    }
    
    /**
     * Constructor básico para solo nombre
     */
    public FavoriteItem(String name) {
        this.name = name;
        this.price = 0.0;
        this.iconUrl = "";
        this.pinned = false;
        this.createdAt = System.currentTimeMillis() / 1000;
    }
    
    // Getters y Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }
    
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
    public boolean isPinned() {
        return pinned;
    }
    
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "FavoriteItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", iconUrl='" + iconUrl + '\'' +
                ", pinned=" + pinned +
                ", createdAt=" + createdAt +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FavoriteItem that = (FavoriteItem) obj;
        return id == that.id || (name != null && name.equals(that.name));
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : (int) id;
    }
}
