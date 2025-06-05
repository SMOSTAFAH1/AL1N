package com.grupo32.al1n.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.grupo32.al1n.R;
import com.grupo32.al1n.models.FavoriteItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para el RecyclerView de cryptos favoritos
 * Maneja la visualización de elementos favoritos con funcionalidad de pin y
 * share
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<FavoriteItem> favoritesList;
    private OnFavoriteInteractionListener listener;

    /**
     * Interface para manejar interacciones con los elementos favoritos
     */
    public interface OnFavoriteInteractionListener {
        void onPinToggle(FavoriteItem favoriteItem, int position);

        void onShare(FavoriteItem favoriteItem);

        void onDelete(FavoriteItem favoriteItem, int position);

        void onItemClick(FavoriteItem favoriteItem);
    }

    /**
     * Constructor del adapter
     * 
     * @param context       Contexto de la aplicación
     * @param favoritesList Lista de elementos favoritos
     * @param listener      Listener para las interacciones
     */
    public FavoriteAdapter(Context context, List<FavoriteItem> favoritesList, OnFavoriteInteractionListener listener) {
        this.context = context;
        this.favoritesList = favoritesList != null ? favoritesList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteItem favorite = favoritesList.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    /**
     * Actualiza la lista de favoritos
     * 
     * @param newFavorites Nueva lista de favoritos
     */
    public void updateFavorites(List<FavoriteItem> newFavorites) {
        this.favoritesList.clear();
        if (newFavorites != null)
            this.favoritesList.addAll(newFavorites);
        notifyDataSetChanged();
    }

    /**
     * Obtiene un elemento favorito por posición
     * 
     * @param position Posición del elemento
     * @return Elemento favorito
     */
    public FavoriteItem getItem(int position) {
        if (position >= 0 && position < favoritesList.size())
            return favoritesList.get(position);
        return null;
    }

    /**
     * Elimina un elemento de la lista
     * 
     * @param position Posición del elemento a eliminar
     */
    public void removeItem(int position) {
        if (position >= 0 && position < favoritesList.size()) {
            favoritesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Actualiza un elemento específico
     * 
     * @param position    Posición del elemento
     * @param updatedItem Elemento actualizado
     */
    public void updateItem(int position, FavoriteItem updatedItem) {
        if (position >= 0 && position < favoritesList.size()) {
            favoritesList.set(position, updatedItem);
            notifyItemChanged(position);
        }
    }

    /**
     * ViewHolder para los elementos del RecyclerView
     */
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCryptoIcon;
        private TextView tvCryptoName;
        private TextView tvCryptoPrice;
        private ImageView ivPinIndicator;
        private ImageView ivShareButton;
        private ImageView btnTogglePin;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar componentes
            ivCryptoIcon = itemView.findViewById(R.id.ivCryptoIcon);
            tvCryptoName = itemView.findViewById(R.id.tvCryptoName);
            tvCryptoPrice = itemView.findViewById(R.id.tvCryptoPrice);
            ivPinIndicator = itemView.findViewById(R.id.ivPinIndicator);
            ivShareButton = itemView.findViewById(R.id.ivShareButton);
            btnTogglePin = itemView.findViewById(R.id.btnTogglePin);

            // Configurar listeners
            setupListeners();
        }

        /**
         * Configura los listeners del ViewHolder
         */
        private void setupListeners() {
            // Click en el item completo para ver detalles
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        FavoriteItem favorite = favoritesList.get(position);
                        listener.onItemClick(favorite);
                    }
                }
            });

            // Click en pin para toggle (usando el botón específico)
            btnTogglePin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        FavoriteItem favorite = favoritesList.get(position);
                        listener.onPinToggle(favorite, position);
                    }
                }
            });

            // Click en share
            ivShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        FavoriteItem favorite = favoritesList.get(position);
                        listener.onShare(favorite);
                    }
                }
            });

            // Long click para compartir (alternativo)
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        FavoriteItem favorite = favoritesList.get(position);
                        listener.onShare(favorite);
                        return true;
                    }
                    return false;
                }
            });
        }

        /**
         * Vincula los datos del favorito con la vista
         * 
         * @param favorite Elemento favorito a mostrar
         */
        public void bind(FavoriteItem favorite) {
            // Configurar nombre
            tvCryptoName.setText(favorite.getName());

            // Configurar precio con formato
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            String formattedPrice = formatter.format(favorite.getPrice());
            tvCryptoPrice.setText(formattedPrice); // Configurar indicador de pin y botón
            if (favorite.isPinned()) {
                ivPinIndicator.setVisibility(View.VISIBLE);
                ivPinIndicator.setColorFilter(context.getResources().getColor(R.color.primary_color));
                btnTogglePin.setImageResource(R.drawable.ic_push_pin);
                btnTogglePin.setColorFilter(context.getResources().getColor(R.color.primary_color));
            } else {
                ivPinIndicator.setVisibility(View.GONE);
                btnTogglePin.setImageResource(R.drawable.ic_push_pin_outline);
                btnTogglePin.setColorFilter(context.getResources().getColor(R.color.gray));
            }

            // Cargar icono con Glide
            if (favorite.getIconUrl() != null && !favorite.getIconUrl().isEmpty()) {
                Glide.with(context)
                        .load(favorite.getIconUrl())
                        .transform(new CircleCrop())
                        .placeholder(R.drawable.ic_crypto_placeholder)
                        .error(R.drawable.ic_crypto_placeholder)
                        .into(ivCryptoIcon);
            } else
                ivCryptoIcon.setImageResource(R.drawable.ic_crypto_placeholder);
        }
    }
}
