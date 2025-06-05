package com.grupo32.al1n.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.grupo32.al1n.R;
import com.grupo32.al1n.CryptoDetailActivity;
import com.grupo32.al1n.adapters.FavoriteAdapter;
import com.grupo32.al1n.adapters.CryptoSearchAdapter;
import com.grupo32.al1n.database.FavoritesDao;
import com.grupo32.al1n.models.FavoriteItem;
import com.grupo32.al1n.models.CryptoItem;
import com.grupo32.al1n.utils.CryptoDataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment para mostrar y gestionar cryptos favoritos
 * Implementa funcionalidad completa con SQLite, swipe gestures y compartir
 */
public class FavoritesFragment extends Fragment implements FavoriteAdapter.OnFavoriteInteractionListener {

    // Componentes de la UI
    private TextView tvFavoritesTitle;
    private RecyclerView rvFavorites;
    private TextView tvEmptyMessage;
    private FloatingActionButton fabAddFavorite;

    // Componentes de datos
    private FavoritesDao favoritesDao;
    private FavoriteAdapter favoriteAdapter;

    /**
     * Constructor vacío requerido
     */
    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Método factory para crear una nueva instancia del fragment
     * 
     * @return Nueva instancia de FavoritesFragment
     */
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflar el layout para este fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar componentes de datos
        initializeData();

        // Inicializar componentes de la UI
        initializeComponents(view);

        // Configurar RecyclerView
        setupRecyclerView();

        // Configurar listeners
        setupListeners();

        // Cargar favoritos
        loadFavorites();
    }

    /**
     * Inicializa los componentes de datos
     */
    private void initializeData() {
        favoritesDao = new FavoritesDao(getContext());
    }

    /**
     * Inicializa los componentes de la UI
     * 
     * @param view Vista raíz del fragment
     */
    private void initializeComponents(View view) {
        tvFavoritesTitle = view.findViewById(R.id.tvFavoritesTitle);
        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);
        fabAddFavorite = view.findViewById(R.id.fabAddFavorite);
    }

    /**
     * Configura el RecyclerView con su adapter y layout manager
     */
    private void setupRecyclerView() {
        favoriteAdapter = new FavoriteAdapter(getContext(), null, this);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setAdapter(favoriteAdapter);

        // Configurar ItemTouchHelper para swipe gestures
        setupSwipeGestures();
    }

    /**
     * Configura los gestos de swipe (izquierda=eliminar, derecha=pin)
     */
    private void setupSwipeGestures() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                return false; // No implementamos drag & drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FavoriteItem favorite = favoriteAdapter.getItem(position);

                if (favorite != null) {
                    if (direction == ItemTouchHelper.LEFT)
                        deleteFavorite(favorite, position);
                    else if (direction == ItemTouchHelper.RIGHT)
                        togglePin(favorite, position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                    int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    if (dX > 0)
                        drawSwipeBackground(c, itemView, dX, Color.parseColor("#4CAF50"),
                                R.drawable.ic_push_pin, true);
                    else if (dX < 0)
                        drawSwipeBackground(c, itemView, dX, Color.parseColor("#F44336"),
                                R.drawable.ic_delete, false);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(rvFavorites);
    }

    /**
     * Dibuja el fondo del swipe con icono
     */
    private void drawSwipeBackground(Canvas c, View itemView, float dX, int backgroundColor,
            int iconResId, boolean isRightSwipe) {
        ColorDrawable background = new ColorDrawable(backgroundColor);

        if (isRightSwipe)
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + (int) dX, itemView.getBottom());
        else
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        background.draw(c);

        // Dibujar icono
        Drawable icon = ContextCompat.getDrawable(getContext(), iconResId);
        if (icon != null) {
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + iconMargin;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (isRightSwipe) {
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = iconLeft + icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            } else {
                int iconRight = itemView.getRight() - iconMargin;
                int iconLeft = iconRight - icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            }
            icon.draw(c);
        }
    }

    /**
     * Configura los listeners de los componentes
     */
    private void setupListeners() {
        // Listener del FloatingActionButton
        fabAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFavoriteDialog();
            }
        });
    }

    /**
     * Muestra el dialog inteligente para buscar y agregar cryptos
     */
    private void showAddFavoriteDialog() {
        // Verificar si hay datos disponibles
        if (!CryptoDataHolder.getInstance().hasData()) {
            Toast.makeText(getContext(),
                    "Por favor espera a que se carguen las criptomonedas del Home",
                    Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Agregar a Favoritos");

        // Crear layout del dialog
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_search_crypto, null);
        TextInputEditText etSearchCrypto = dialogView.findViewById(R.id.etSearchCrypto);
        RecyclerView rvSearchResults = dialogView.findViewById(R.id.rvSearchResults);
        TextView tvNoResults = dialogView.findViewById(R.id.tvNoResults);

        builder.setView(dialogView);
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();

        // Configurar RecyclerView de búsqueda
        List<CryptoItem> searchResults = new ArrayList<>();
        CryptoSearchAdapter searchAdapter = new CryptoSearchAdapter(getContext(), searchResults, crypto -> {
            addCryptoToFavorites(crypto);
            dialog.dismiss();
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(searchAdapter);        // Configurar búsqueda en tiempo real
        etSearchCrypto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No necesitamos hacer nada antes del cambio de texto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();

                if (query.isEmpty()) {
                    // Mostrar mensaje inicial
                    rvSearchResults.setVisibility(View.GONE);
                    tvNoResults.setVisibility(View.VISIBLE);
                    tvNoResults.setText("Escribe para buscar criptomonedas");
                } else {
                    // Buscar cryptos
                    List<CryptoItem> results = CryptoDataHolder.getInstance().searchCryptos(query);

                    if (results.isEmpty()) {
                        rvSearchResults.setVisibility(View.GONE);
                        tvNoResults.setVisibility(View.VISIBLE);
                        tvNoResults.setText("No se encontraron resultados para \"" + query + "\"");
                    } else {
                        tvNoResults.setVisibility(View.GONE);
                        rvSearchResults.setVisibility(View.VISIBLE);
                        searchAdapter.updateList(results);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No necesitamos hacer nada después del cambio de texto
            }
        });

        dialog.show();
    }

    /**
     * Agrega una crypto seleccionada a favoritos
     * 
     * @param crypto La crypto seleccionada del autocompletado
     */
    private void addCryptoToFavorites(CryptoItem crypto) {
        // Verificar si ya está en favoritos
        if (favoritesDao.isFavorite(crypto.getSymbol())) {
            Toast.makeText(getContext(),
                    crypto.getName() + " ya está en favoritos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear FavoriteItem desde CryptoItem
        FavoriteItem favoriteItem = new FavoriteItem(
                crypto.getSymbol(),
                crypto.getName(),
                crypto.getCurrentPrice(),
                crypto.getImageUrl(),
                false // no está pinned por defecto
        );

        // Insertar en la base de datos
        long result = favoritesDao.insertFavorite(favoriteItem);
        if (result != -1) {
            Toast.makeText(getContext(),
                    crypto.getName() + " agregado a favoritos",
                    Toast.LENGTH_SHORT).show();
            loadFavorites(); // Recargar la lista
        } else {
            Toast.makeText(getContext(),
                    "Error al agregar " + crypto.getName() + " a favoritos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga los favoritos desde la base de datos
     */
    private void loadFavorites() {
        List<FavoriteItem> favorites = favoritesDao.getAllFavorites();
        favoriteAdapter.updateFavorites(favorites);

        // Mostrar/ocultar mensaje vacío
        if (favorites.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            tvEmptyMessage.setVisibility(View.GONE);
        }
    }

    /**
     * Elimina un favorito
     */
    private void deleteFavorite(FavoriteItem favorite, int position) {
        int result = favoritesDao.deleteFavorite(favorite.getId());
        if (result > 0) {
            favoriteAdapter.removeItem(position);
            Toast.makeText(getContext(), "Favorito eliminado", Toast.LENGTH_SHORT).show();

            // Verificar si la lista está vacía
            if (favoriteAdapter.getItemCount() == 0) {
                rvFavorites.setVisibility(View.GONE);
                tvEmptyMessage.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            favoriteAdapter.notifyItemChanged(position); // Restaurar item
        }
    }

    /**
     * Toggle del estado de pin
     */
    private void togglePin(FavoriteItem favorite, int position) {
        favorite.setPinned(!favorite.isPinned());
        int result = favoritesDao.updateFavorite(favorite);

        if (result > 0) {
            // Recargar lista para reordenar (pinned first)
            loadFavorites();
            String message = favorite.isPinned() ? "Crypto fijado" : "Crypto desfijado";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            favoriteAdapter.notifyItemChanged(position); // Restaurar item
        }
    }

    // Implementación de FavoriteAdapter.OnFavoriteInteractionListener

    @Override
    public void onPinToggle(FavoriteItem favoriteItem, int position) {
        togglePin(favoriteItem, position);
    }

    @Override
    public void onShare(FavoriteItem favoriteItem) {
        String shareText = String.format("¡Mira esta crypto! %s está a $%.2f",
                favoriteItem.getName(), favoriteItem.getPrice());

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Crypto Favorito - AL1N");

        startActivity(Intent.createChooser(shareIntent, "Compartir crypto"));
    }

    @Override
    public void onDelete(FavoriteItem favoriteItem, int position) {
        deleteFavorite(favoriteItem, position);
    }

    @Override
    public void onItemClick(FavoriteItem favoriteItem) {
        // Crear Intent para abrir CryptoDetailActivity
        Intent intent = new Intent(getContext(), CryptoDetailActivity.class);

        // Pasar los datos disponibles del favorito
        intent.putExtra("crypto_symbol", favoriteItem.getSymbol());
        intent.putExtra("crypto_name", favoriteItem.getName());
        intent.putExtra("crypto_price", favoriteItem.getPrice());

        // Para los campos que no están en FavoriteItem, usar valores por defecto
        intent.putExtra("crypto_change", 0.0); // No tenemos el cambio de precio en favoritos
        intent.putExtra("crypto_market_cap", 0.0); // No tenemos market cap en favoritos
        intent.putExtra("crypto_volume", 0.0); // No tenemos volumen en favoritos
        intent.putExtra("crypto_rank", 0); // No tenemos rank en favoritos

        startActivity(intent);
    }
}
