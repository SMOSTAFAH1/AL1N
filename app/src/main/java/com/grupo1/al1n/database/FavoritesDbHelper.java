package com.grupo1.al1n.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper para la base de datos SQLite de favoritos
 * Maneja la creación, actualización y estructura de la tabla favorites
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {
      // Información de la base de datos
    private static final String DATABASE_NAME = "al1n_favorites.db";
    private static final int DATABASE_VERSION = 2; // Incrementar versión para agregar symbol
    
    // Nombre de la tabla
    public static final String TABLE_FAVORITES = "favorites";
    
    // Columnas de la tabla
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_ICON_URL = "iconUrl";
    public static final String COLUMN_PINNED = "pinned";
    public static final String COLUMN_CREATED_AT = "created_at";
    
    // SQL para crear la tabla
    private static final String SQL_CREATE_TABLE = 
        "CREATE TABLE " + TABLE_FAVORITES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SYMBOL + " TEXT NOT NULL UNIQUE, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_PRICE + " REAL DEFAULT 0.0, " +
            COLUMN_ICON_URL + " TEXT, " +
            COLUMN_PINNED + " INTEGER DEFAULT 0, " +
            COLUMN_CREATED_AT + " INTEGER DEFAULT (strftime('%s','now'))" +
        ")";
    
    // SQL para eliminar la tabla
    private static final String SQL_DELETE_TABLE = 
        "DROP TABLE IF EXISTS " + TABLE_FAVORITES;
    
    /**
     * Constructor del helper
     * @param context Contexto de la aplicación
     */
    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de favoritos
        db.execSQL(SQL_CREATE_TABLE);
        
        // Insertar algunos datos de ejemplo (opcional)
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Por simplicidad, eliminar y recrear la tabla
        // En producción, deberías hacer migraciones más sofisticadas
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
      /**
     * Inserta datos de ejemplo en la tabla de favoritos
     * @param db Base de datos donde insertar
     */
    private void insertSampleData(SQLiteDatabase db) {
        // Insertar algunos favoritos de ejemplo
        String insertBTC = "INSERT INTO " + TABLE_FAVORITES + 
            " (" + COLUMN_SYMBOL + ", " + COLUMN_NAME + ", " + COLUMN_PRICE + ", " + COLUMN_ICON_URL + ", " + COLUMN_PINNED + ") " +
            "VALUES ('BTC', 'Bitcoin', 67234.50, 'https://s2.coinmarketcap.com/static/img/coins/128x128/1.png', 1)";
        
        String insertETH = "INSERT INTO " + TABLE_FAVORITES + 
            " (" + COLUMN_SYMBOL + ", " + COLUMN_NAME + ", " + COLUMN_PRICE + ", " + COLUMN_ICON_URL + ", " + COLUMN_PINNED + ") " +
            "VALUES ('ETH', 'Ethereum', 2456.78, 'https://s2.coinmarketcap.com/static/img/coins/128x128/1027.png', 0)";
        
        try {
            db.execSQL(insertBTC);
            db.execSQL(insertETH);
        } catch (Exception e) {
            // Ignorar errores de inserción (pueden existir duplicados)
            e.printStackTrace();
        }
    }
}
