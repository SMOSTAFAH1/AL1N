package com.grupo1.al1n.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.grupo1.al1n.models.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para manejar operaciones CRUD de favoritos
 * Proporciona métodos para insertar, leer, actualizar y eliminar favoritos
 */
public class FavoritesDao {
    
    private static final String TAG = "FavoritesDao";
    
    private FavoritesDbHelper dbHelper;
    
    /**
     * Constructor del DAO
     * @param context Contexto de la aplicación
     */
    public FavoritesDao(Context context) {
        dbHelper = new FavoritesDbHelper(context);
    }
    
    /**
     * Inserta un nuevo favorito en la base de datos
     * @param favorite Favorito a insertar
     * @return ID del favorito insertado, -1 si hay error
     */
    public long insertFavorite(FavoriteItem favorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_NAME, favorite.getName());
        values.put(FavoritesDbHelper.COLUMN_PRICE, favorite.getPrice());
        values.put(FavoritesDbHelper.COLUMN_ICON_URL, favorite.getIconUrl());
        values.put(FavoritesDbHelper.COLUMN_PINNED, favorite.isPinned() ? 1 : 0);
        
        long id = db.insert(FavoritesDbHelper.TABLE_FAVORITES, null, values);
        
        if (id != -1) {
            Log.d(TAG, "Favorito insertado: " + favorite.getName() + " con ID: " + id);
        } else {
            Log.e(TAG, "Error al insertar favorito: " + favorite.getName());
        }
        
        db.close();
        return id;
    }
    
    /**
     * Obtiene todos los favoritos ordenados (pinned primero, luego por fecha)
     * @return Lista de favoritos ordenada
     */
    public List<FavoriteItem> getAllFavorites() {
        List<FavoriteItem> favorites = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Ordenar: pinned primero (DESC), luego por fecha de creación (DESC)
        String orderBy = FavoritesDbHelper.COLUMN_PINNED + " DESC, " + 
                        FavoritesDbHelper.COLUMN_CREATED_AT + " DESC";
        
        Cursor cursor = db.query(
            FavoritesDbHelper.TABLE_FAVORITES,
            null, // todas las columnas
            null, // sin WHERE
            null, // sin argumentos WHERE
            null, // sin GROUP BY
            null, // sin HAVING
            orderBy
        );
        
        if (cursor.moveToFirst()) {
            do {
                FavoriteItem favorite = cursorToFavorite(cursor);
                favorites.add(favorite);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        Log.d(TAG, "Favoritos cargados: " + favorites.size());
        return favorites;
    }
    
    /**
     * Obtiene un favorito por nombre
     * @param name Nombre del favorito
     * @return Favorito encontrado o null si no existe
     */
    public FavoriteItem getFavoriteByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = FavoritesDbHelper.COLUMN_NAME + " = ?";
        String[] selectionArgs = { name };
        
        Cursor cursor = db.query(
            FavoritesDbHelper.TABLE_FAVORITES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        FavoriteItem favorite = null;
        if (cursor.moveToFirst()) {
            favorite = cursorToFavorite(cursor);
        }
        
        cursor.close();
        db.close();
        
        return favorite;
    }
    
    /**
     * Actualiza el estado de pinned de un favorito
     * @param id ID del favorito
     * @param pinned Nuevo estado de pinned
     * @return Número de filas afectadas
     */
    public int updatePinnedStatus(long id, boolean pinned) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_PINNED, pinned ? 1 : 0);
        
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        
        int rowsAffected = db.update(
            FavoritesDbHelper.TABLE_FAVORITES,
            values,
            whereClause,
            whereArgs
        );
        
        Log.d(TAG, "Favorito " + id + " actualizado. Pinned: " + pinned);
        
        db.close();
        return rowsAffected;
    }
    
    /**
     * Actualiza el precio de un favorito
     * @param id ID del favorito
     * @param price Nuevo precio
     * @return Número de filas afectadas
     */
    public int updatePrice(long id, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_PRICE, price);
        
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        
        int rowsAffected = db.update(
            FavoritesDbHelper.TABLE_FAVORITES,
            values,
            whereClause,
            whereArgs
        );
        
        db.close();
        return rowsAffected;
    }
    
    /**
     * Elimina un favorito por ID
     * @param id ID del favorito a eliminar
     * @return Número de filas eliminadas
     */
    public int deleteFavorite(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        
        int rowsDeleted = db.delete(
            FavoritesDbHelper.TABLE_FAVORITES,
            whereClause,
            whereArgs
        );
        
        Log.d(TAG, "Favorito eliminado. ID: " + id + ", Filas afectadas: " + rowsDeleted);
        
        db.close();
        return rowsDeleted;
    }
    
    /**
     * Elimina un favorito por nombre
     * @param name Nombre del favorito a eliminar
     * @return Número de filas eliminadas
     */
    public int deleteFavoriteByName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        String whereClause = FavoritesDbHelper.COLUMN_NAME + " = ?";
        String[] whereArgs = { name };
        
        int rowsDeleted = db.delete(
            FavoritesDbHelper.TABLE_FAVORITES,
            whereClause,
            whereArgs
        );
        
        Log.d(TAG, "Favorito eliminado. Nombre: " + name + ", Filas afectadas: " + rowsDeleted);
        
        db.close();
        return rowsDeleted;
    }
    
    /**
     * Verifica si un favorito existe por nombre
     * @param name Nombre del favorito
     * @return true si existe, false si no
     */
    public boolean favoriteExists(String name) {
        return getFavoriteByName(name) != null;
    }
    
    /**
     * Obtiene el número total de favoritos
     * @return Número de favoritos
     */
    public int getFavoritesCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + FavoritesDbHelper.TABLE_FAVORITES, null);
        int count = 0;
        
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        
        cursor.close();
        db.close();
        
        return count;
    }
    
    /**
     * Convierte un cursor en un objeto FavoriteItem
     * @param cursor Cursor de la base de datos
     * @return Objeto FavoriteItem
     */
    private FavoriteItem cursorToFavorite(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_PRICE));
        String iconUrl = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_ICON_URL));
        boolean pinned = cursor.getInt(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_PINNED)) == 1;
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_CREATED_AT));
        
        return new FavoriteItem(id, name, price, iconUrl, pinned, createdAt);
    }
      /**
     * Actualiza un favorito completo
     * @param favorite Favorito con datos actualizados
     * @return Número de filas afectadas
     */
    public int updateFavorite(FavoriteItem favorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_NAME, favorite.getName());
        values.put(FavoritesDbHelper.COLUMN_PRICE, favorite.getPrice());
        values.put(FavoritesDbHelper.COLUMN_ICON_URL, favorite.getIconUrl());
        values.put(FavoritesDbHelper.COLUMN_PINNED, favorite.isPinned() ? 1 : 0);
        
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(favorite.getId()) };
        
        int rowsAffected = db.update(
            FavoritesDbHelper.TABLE_FAVORITES,
            values,
            whereClause,
            whereArgs
        );
        
        Log.d(TAG, "Favorito actualizado. ID: " + favorite.getId() + ", Filas afectadas: " + rowsAffected);
        
        db.close();
        return rowsAffected;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
