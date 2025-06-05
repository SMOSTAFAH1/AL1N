package com.grupo32.al1n.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.grupo32.al1n.models.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para manejar operaciones CRUD de favoritos
 * Proporciona métodos para insertar, leer, actualizar y eliminar favoritos
 */
public class FavoritesDao {

    private static final String TAG = "FavoritesDao";
    private static final String PREFS_NAME = "AL1N_Prefs";
    private static final String KEY_ACTIVE_USER = "active_user";

    private FavoritesDbHelper dbHelper;
    private String activeUser;

    /**
     * Constructor del DAO
     * 
     * @param context Contexto de la aplicación
     */
    public FavoritesDao(Context context) {
        dbHelper = new FavoritesDbHelper(context);
        // Obtener el usuario activo
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        activeUser = sharedPreferences.getString(KEY_ACTIVE_USER, "default_user");
    }

    /**
     * Establece el usuario activo
     * 
     * @param username Nombre del usuario
     */
    public void setActiveUser(String username) {
        this.activeUser = username.isEmpty() ? "default_user" : username;
    }

    /**
     * Inserta un nuevo favorito en la base de datos
     * 
     * @param favorite Favorito a insertar
     * @return ID del favorito insertado, -1 si hay error
     */
    public long insertFavorite(FavoriteItem favorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_USERNAME, activeUser);
        values.put(FavoritesDbHelper.COLUMN_SYMBOL, favorite.getSymbol());
        values.put(FavoritesDbHelper.COLUMN_NAME, favorite.getName());
        values.put(FavoritesDbHelper.COLUMN_PRICE, favorite.getPrice());
        values.put(FavoritesDbHelper.COLUMN_ICON_URL, favorite.getIconUrl());
        values.put(FavoritesDbHelper.COLUMN_PINNED, favorite.isPinned() ? 1 : 0);

        long id = db.insert(FavoritesDbHelper.TABLE_FAVORITES, null, values);

        if (id != -1)
            Log.d(TAG, "Favorito insertado: " + favorite.getSymbol() + " - " + favorite.getName() + " con ID: " + id);
        else
            Log.e(TAG, "Error al insertar favorito: " + favorite.getSymbol() + " - " + favorite.getName());

        db.close();
        return id;
    }

    /**
     * Obtiene todos los favoritos ordenados (pinned primero, luego por fecha)
     * 
     * @return Lista de favoritos ordenada
     */
    public List<FavoriteItem> getAllFavorites() {
        List<FavoriteItem> favorites = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Ordenar: pinned primero (DESC), luego por fecha de creación (DESC)
        String orderBy = FavoritesDbHelper.COLUMN_PINNED + " DESC, " +
                FavoritesDbHelper.COLUMN_CREATED_AT + " DESC";

        // Filtrar por usuario activo
        String selection = FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { activeUser };

        Cursor cursor = db.query(
                FavoritesDbHelper.TABLE_FAVORITES,
                null, // todas las columnas
                selection, // WHERE username = ?
                selectionArgs, // parámetros para WHERE
                null, // sin GROUP BY
                null, // sin HAVING
                orderBy);

        if (cursor.moveToFirst()) {
            do {
                FavoriteItem favorite = cursorToFavorite(cursor);
                favorites.add(favorite);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Log.d(TAG, "Favoritos cargados para " + activeUser + ": " + favorites.size());
        return favorites;
    }

    /**
     * Obtiene un favorito por nombre
     * 
     * @param name Nombre del favorito
     * @return Favorito encontrado o null si no existe
     */
    public FavoriteItem getFavoriteByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = FavoritesDbHelper.COLUMN_NAME + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { name, activeUser };

        Cursor cursor = db.query(
                FavoritesDbHelper.TABLE_FAVORITES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        FavoriteItem favorite = null;
        if (cursor.moveToFirst())
            favorite = cursorToFavorite(cursor);

        cursor.close();
        db.close();

        return favorite;
    }

    /**
     * Actualiza el estado de pinned de un favorito
     * 
     * @param id     ID del favorito
     * @param pinned Nuevo estado de pinned
     * @return Número de filas afectadas
     */
    public int updatePinnedStatus(long id, boolean pinned) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_PINNED, pinned ? 1 : 0);

        // Asegurarse de que sea un favorito del usuario activo
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] whereArgs = { String.valueOf(id), activeUser };

        int rowsAffected = db.update(
                FavoritesDbHelper.TABLE_FAVORITES,
                values,
                whereClause,
                whereArgs);

        Log.d(TAG, "Favorito " + id + " actualizado. Pinned: " + pinned);

        db.close();
        return rowsAffected;
    }

    /**
     * Actualiza el precio de un favorito
     * 
     * @param id    ID del favorito
     * @param price Nuevo precio
     * @return Número de filas afectadas
     */
    public int updatePrice(long id, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_PRICE, price);

        // Asegurarse de que sea un favorito del usuario activo
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] whereArgs = { String.valueOf(id), activeUser };

        int rowsAffected = db.update(
                FavoritesDbHelper.TABLE_FAVORITES,
                values,
                whereClause,
                whereArgs);

        db.close();
        return rowsAffected;
    }

    /**
     * Elimina un favorito por ID
     * 
     * @param id ID del favorito a eliminar
     * @return Número de filas eliminadas
     */
    public int deleteFavorite(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Asegurarse de que sea un favorito del usuario activo
        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] whereArgs = { String.valueOf(id), activeUser };

        int rowsDeleted = db.delete(
                FavoritesDbHelper.TABLE_FAVORITES,
                whereClause,
                whereArgs);

        Log.d(TAG, "Favorito eliminado. ID: " + id + ", Filas afectadas: " + rowsDeleted);

        db.close();
        return rowsDeleted;
    }

    /**
     * Elimina un favorito por nombre
     * 
     * @param name Nombre del favorito a eliminar
     * @return Número de filas eliminadas
     */
    public int deleteFavoriteByName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Asegurarse de que sea un favorito del usuario activo
        String whereClause = FavoritesDbHelper.COLUMN_NAME + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] whereArgs = { name, activeUser };

        int rowsDeleted = db.delete(
                FavoritesDbHelper.TABLE_FAVORITES,
                whereClause,
                whereArgs);

        Log.d(TAG, "Favorito eliminado. Nombre: " + name + ", Filas afectadas: " + rowsDeleted);

        db.close();
        return rowsDeleted;
    }

    /**
     * Verifica si un favorito existe por nombre
     * 
     * @param name Nombre del favorito
     * @return true si existe, false si no
     */
    public boolean favoriteExists(String name) {
        return getFavoriteByName(name) != null;
    }

    /**
     * Obtiene el número total de favoritos para el usuario activo
     * 
     * @return Número de favoritos
     */
    public int getFavoritesCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] args = { activeUser };
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + FavoritesDbHelper.TABLE_FAVORITES +
                        " WHERE " + FavoritesDbHelper.COLUMN_USERNAME + " = ?",
                args);
        int count = 0;

        if (cursor.moveToFirst())
            count = cursor.getInt(0);

        cursor.close();
        db.close();

        return count;
    }

    /**
     * Convierte un cursor en un objeto FavoriteItem
     * 
     * @param cursor Cursor de la base de datos
     * @return Objeto FavoriteItem
     */
    private FavoriteItem cursorToFavorite(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_ID));
        String symbol = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_SYMBOL));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_PRICE));
        String iconUrl = cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_ICON_URL));
        boolean pinned = cursor.getInt(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_PINNED)) == 1;
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_CREATED_AT));

        return new FavoriteItem(id, symbol, name, price, iconUrl, pinned, createdAt);
    }

    /**
     * Actualiza un favorito completo
     * 
     * @param favorite Favorito con datos actualizados
     * @return Número de filas afectadas
     */
    public int updateFavorite(FavoriteItem favorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoritesDbHelper.COLUMN_SYMBOL, favorite.getSymbol());
        values.put(FavoritesDbHelper.COLUMN_NAME, favorite.getName());
        values.put(FavoritesDbHelper.COLUMN_PRICE, favorite.getPrice());
        values.put(FavoritesDbHelper.COLUMN_ICON_URL, favorite.getIconUrl());
        values.put(FavoritesDbHelper.COLUMN_PINNED, favorite.isPinned() ? 1 : 0);

        String whereClause = FavoritesDbHelper.COLUMN_ID + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] whereArgs = { String.valueOf(favorite.getId()), activeUser };

        int rowsAffected = db.update(
                FavoritesDbHelper.TABLE_FAVORITES,
                values,
                whereClause,
                whereArgs);

        Log.d(TAG, "Favorito actualizado. ID: " + favorite.getId() + ", Filas afectadas: " + rowsAffected);

        db.close();
        return rowsAffected;
    }

    /**
     * Verifica si una criptomoneda está en favoritos por símbolo
     * 
     * @param symbol Símbolo de la criptomoneda (ej: BTC, ETH)
     * @return true si está en favoritos, false si no
     */
    public boolean isFavorite(String symbol) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = FavoritesDbHelper.COLUMN_SYMBOL + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { symbol, activeUser };

        Cursor cursor = db.query(
                FavoritesDbHelper.TABLE_FAVORITES,
                new String[] { FavoritesDbHelper.COLUMN_ID }, // Solo necesitamos el ID
                selection,
                selectionArgs,
                null,
                null,
                null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }

    /**
     * Elimina un favorito por símbolo
     * 
     * @param symbol Símbolo de la criptomoneda a eliminar
     * @return Número de filas eliminadas
     */
    public int deleteFavoriteBySymbol(String symbol) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = FavoritesDbHelper.COLUMN_SYMBOL + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] whereArgs = { symbol, activeUser };

        int rowsDeleted = db.delete(
                FavoritesDbHelper.TABLE_FAVORITES,
                whereClause,
                whereArgs);

        Log.d(TAG, "Favorito eliminado. Símbolo: " + symbol + ", Filas afectadas: " + rowsDeleted);

        db.close();
        return rowsDeleted;
    }

    /**
     * Obtiene un favorito por símbolo
     * 
     * @param symbol Símbolo de la criptomoneda
     * @return Favorito encontrado o null si no existe
     */
    public FavoriteItem getFavoriteBySymbol(String symbol) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = FavoritesDbHelper.COLUMN_SYMBOL + " = ? AND " +
                FavoritesDbHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { symbol, activeUser };

        Cursor cursor = db.query(
                FavoritesDbHelper.TABLE_FAVORITES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        FavoriteItem favorite = null;
        if (cursor.moveToFirst())
            favorite = cursorToFavorite(cursor);

        cursor.close();
        db.close();

        return favorite;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void close() {
        if (dbHelper != null)
            dbHelper.close();
    }
}
