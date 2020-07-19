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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.amiibos.Statics.AMIIBO_ITEM;
import static com.example.amiibos.Statics.SHARED_PREFS;

public class AmiiboAdapter extends RecyclerView.Adapter<AmiiboAdapter.AmiiboViewHolder> {

    private Context context;
    private List<Amiibo_> amiibos = new ArrayList<>();

    private SharedPreferences sharedPreferences;

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
        holder.bind(amiibos.get(position));
    }

    @Override
    public int getItemCount() {
        return amiibos.size();
    }


    public void setAmiibos(List<Amiibo_> amiibos) {
        this.amiibos = amiibos;
    }

    public List<Amiibo_> getAmiibos() {
        return amiibos;
    }

    public class AmiiboViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView amiiboImageView;
        private TextView amiiboCharacter;
        private TextView purchaseIndicator;

        public AmiiboViewHolder(@NonNull View itemView) {
            super(itemView);
            amiiboImageView = itemView.findViewById(R.id.amiibos_image_view);
            amiiboCharacter = itemView.findViewById(R.id.amiibo_name_text_view);
            purchaseIndicator = itemView.findViewById(R.id.purchase_indicator_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(Amiibo_ amiibo) {
            if (sharedPreferences.getBoolean(amiibo.getHead() + amiibo.getTail(), false)) {
                purchaseIndicator.setVisibility(View.VISIBLE);
            } else {
                purchaseIndicator.setVisibility(View.INVISIBLE);
            }
            amiiboCharacter.setText(amiibo.getCharacter());
            Picasso.with(context).load(amiibo.getImage()).fit().centerInside().into(amiiboImageView);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            final Amiibo_ currentItem = amiibos.get(itemPosition);

            Gson gson = new Gson();
            Intent intent = new Intent(context, AmiiboDetailsActivity.class);
            intent.putExtra(AMIIBO_ITEM, gson.toJson(currentItem));
            context.startActivity(intent);
        }
    }
}
