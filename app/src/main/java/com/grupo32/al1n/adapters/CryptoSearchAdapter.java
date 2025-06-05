package com.grupo32.al1n.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grupo32.al1n.R;
import com.grupo32.al1n.models.CryptoItem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter para mostrar cryptos en el diálogo de búsqueda/autocompletado
 */
public class CryptoSearchAdapter extends RecyclerView.Adapter<CryptoSearchAdapter.SearchViewHolder> {

    private List<CryptoItem> cryptoList;
    private Context context;
    private OnCryptoSelectedListener listener;
    private DecimalFormat decimalFormat;

    public interface OnCryptoSelectedListener {
        void onCryptoSelected(CryptoItem crypto);
    }

    public CryptoSearchAdapter(Context context, List<CryptoItem> cryptoList, OnCryptoSelectedListener listener) {
        this.context = context;
        this.cryptoList = cryptoList;
        this.listener = listener;
        this.decimalFormat = new DecimalFormat("#,##0.00");
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crypto_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        CryptoItem crypto = cryptoList.get(position);

        holder.symbolTextView.setText(crypto.getSymbol());
        holder.nameTextView.setText(crypto.getName());
        holder.priceTextView.setText("$" + decimalFormat.format(crypto.getPrice()));

        // Cargar imagen de la criptomoneda
        if (crypto.getImageUrl() != null && !crypto.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(crypto.getImageUrl())
                    .placeholder(R.drawable.ic_crypto_placeholder)
                    .error(R.drawable.ic_crypto_placeholder)
                    .circleCrop()
                    .into(holder.iconImageView);
        } else
            holder.iconImageView.setImageResource(R.drawable.ic_crypto_placeholder);

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onCryptoSelected(crypto);
        });
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    /**
     * Actualiza la lista de cryptos filtradas
     */
    public void updateList(List<CryptoItem> newList) {
        this.cryptoList = newList;
        notifyDataSetChanged();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView symbolTextView;
        TextView nameTextView;
        TextView priceTextView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.ivCryptoIconSearch);
            symbolTextView = itemView.findViewById(R.id.tvCryptoSymbolSearch);
            nameTextView = itemView.findViewById(R.id.tvCryptoNameSearch);
            priceTextView = itemView.findViewById(R.id.tvCryptoPriceSearch);
        }
    }
}
