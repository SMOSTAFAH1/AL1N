package com.grupo32.al1n.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo32.al1n.R;
import com.grupo32.al1n.CryptoDetailActivity;
import com.grupo32.al1n.models.CryptoItem;
import com.grupo32.al1n.models.FavoriteItem;
import com.grupo32.al1n.database.FavoritesDao;
import com.bumptech.glide.Glide;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    private List<CryptoItem> cryptoList;
    private Context context;
    private DecimalFormat decimalFormat;
    private FavoritesDao favoritesDao;

    public CryptoAdapter(Context context, List<CryptoItem> cryptoList) {
        this.context = context;
        this.cryptoList = cryptoList;
        this.decimalFormat = new DecimalFormat("#,##0.00");
        this.favoritesDao = new FavoritesDao(context);
    }

    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crypto, parent, false);
        return new CryptoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        CryptoItem crypto = cryptoList.get(position);
        holder.symbolTextView.setText(crypto.getSymbol());
        holder.nameTextView.setText(crypto.getName());
        holder.priceTextView.setText("$" + decimalFormat.format(crypto.getCurrentPrice()));
        // Cargar imagen de la criptomoneda usando Glide
        if (crypto.getImageUrl() != null && !crypto.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(crypto.getImageUrl())
                    .placeholder(R.drawable.ic_circle_placeholder) // Imagen por defecto mientras carga
                    .error(R.drawable.ic_circle_placeholder) // Imagen por defecto si hay error
                    .circleCrop() // Hacer la imagen circular
                    .into(holder.iconImageView);
        } else
            holder.iconImageView.setImageResource(R.drawable.ic_circle_placeholder);

        // Format price change
        double priceChange = crypto.getPriceChangePercentage24h();
        holder.priceChangeTextView.setText(String.format("%.2f%%", priceChange));

        // Set color based on price change
        int colorRes = priceChange >= 0 ? R.color.green : R.color.red;
        holder.priceChangeTextView.setTextColor(context.getResources().getColor(colorRes));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CryptoDetailActivity.class);
            intent.putExtra("crypto_symbol", crypto.getSymbol());
            intent.putExtra("crypto_name", crypto.getName());
            intent.putExtra("crypto_price", crypto.getCurrentPrice());
            intent.putExtra("crypto_change", crypto.getPriceChangePercentage24h());
            intent.putExtra("crypto_market_cap", crypto.getMarketCap());
            intent.putExtra("crypto_volume", crypto.getTotalVolume());
            intent.putExtra("crypto_rank", crypto.getRank());
            context.startActivity(intent);
        });
        // Set favorite button click listener
        holder.favoriteButton.setOnClickListener(v -> {
            // Verificar si ya está en favoritos
            boolean isCurrentlyFavorite = favoritesDao.isFavorite(crypto.getSymbol());

            if (isCurrentlyFavorite) {
                // Remover de favoritos
                favoritesDao.deleteFavoriteBySymbol(crypto.getSymbol());
                crypto.setFavorite(false);
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);

                // Mostrar mensaje
                Toast.makeText(context, crypto.getName() + " removido de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                FavoriteItem favoriteItem = new FavoriteItem(
                        crypto.getSymbol(),
                        crypto.getName(),
                        crypto.getCurrentPrice(),
                        crypto.getImageUrl(),
                        false // no está pinned por defecto
                );

                long result = favoritesDao.insertFavorite(favoriteItem);
                if (result != -1) {
                    crypto.setFavorite(true);
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite);

                    // Mostrar mensaje
                    Toast.makeText(context, crypto.getName() + " agregado a favoritos", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Error al agregar a favoritos", Toast.LENGTH_SHORT).show();
            }
        });

        // Set favorite button state based on database
        boolean isFavorite = favoritesDao.isFavorite(crypto.getSymbol());
        crypto.setFavorite(isFavorite);
        int iconRes = isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        holder.favoriteButton.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    private String formatLargeNumber(double number) {
        if (number >= 1_000_000_000)
            return decimalFormat.format(number / 1_000_000_000) + "B";
        else if (number >= 1_000_000)
            return decimalFormat.format(number / 1_000_000) + "M";
        else if (number >= 1_000)
            return decimalFormat.format(number / 1_000) + "K";
        else
            return decimalFormat.format(number);
    }

    public void updateCryptoList(List<CryptoItem> newCryptoList) {
        this.cryptoList = newCryptoList;
        notifyDataSetChanged();
    }

    public static class CryptoViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView symbolTextView;
        TextView nameTextView;
        TextView priceTextView;
        TextView priceChangeTextView;
        ImageButton favoriteButton;

        public CryptoViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.ivCryptoIcon);
            symbolTextView = itemView.findViewById(R.id.tvCryptoSymbol);
            nameTextView = itemView.findViewById(R.id.tvCryptoName);
            priceTextView = itemView.findViewById(R.id.tvCryptoPrice);
            priceChangeTextView = itemView.findViewById(R.id.tvCryptoPriceChange);
            favoriteButton = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
