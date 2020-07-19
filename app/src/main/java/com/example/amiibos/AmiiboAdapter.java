package com.example.amiibos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amiibos.Models.Amiibo_;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AmiiboAdapter extends RecyclerView.Adapter<AmiiboAdapter.AmiiboViewHolder> {

    private Context context;
    private List<Amiibo_> amiibos = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    public void setAmiibos(List<Amiibo_> amiibos) {
        this.amiibos = amiibos;
    }

    public List<Amiibo_> getAmiibos() {
        return amiibos;
    }

    public AmiiboAdapter(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AmiiboViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.amiibo_item, viewGroup, false);
        return new AmiiboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmiiboViewHolder holder, int position) {
        Amiibo_ currentItem = amiibos.get(position);

        String imageUrl = currentItem.getImage();
        String amiiboName = currentItem.getCharacter();

        if (sharedPreferences.getBoolean(currentItem.getHead() + currentItem.getTail(), false)) {
            holder.purchaseIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.purchaseIndicator.setVisibility(View.INVISIBLE);
        }
        holder.amiiboName.setText(amiiboName);
        Picasso.with(context).load(imageUrl).fit().centerInside().into(holder.amiiboImageView);
    }

    @Override
    public int getItemCount() {
        return amiibos.size();
    }

    public class AmiiboViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView amiiboImageView;
        private TextView amiiboName;
        private TextView purchaseIndicator;

        public AmiiboViewHolder(@NonNull View itemView) {
            super(itemView);
            amiiboImageView = itemView.findViewById(R.id.amiibos_image_text_view);
            amiiboName = itemView.findViewById(R.id.amiibo_name_text_view);
            purchaseIndicator = itemView.findViewById(R.id.purchase_indicator);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            final Amiibo_ currentItem = amiibos.get(itemPosition);

            Intent intent = new Intent(context, AmiiboDetailsActivity.class);
            intent.putExtra("amiibo_series", currentItem.getAmiiboSeries());
            intent.putExtra("character", currentItem.getCharacter());
            intent.putExtra("game_series", currentItem.getGameSeries());
            intent.putExtra("head", currentItem.getHead());
            intent.putExtra("tail", currentItem.getTail());
            intent.putExtra("type", currentItem.getType());
            context.startActivity(intent);
        }
    }
}
