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

public class AmiiboAdapter extends RecyclerView.Adapter<AmiiboAdapter.AmiiboViewHolder> {

    private Context mContext;
    private ArrayList<Amiibo_> mAmiibosList = new ArrayList<>();

    private SharedPreferences mSharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    public void setmAmiibosList(ArrayList<Amiibo_> mAmiibosList) {
        this.mAmiibosList = mAmiibosList;
    }

    public ArrayList<Amiibo_> getmAmiibosList() {
        return mAmiibosList;
    }

    public AmiiboAdapter(Context context) {
        this.mContext = context;
        this.mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AmiiboViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.amiibo_item, viewGroup, false);
        return new AmiiboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmiiboViewHolder holder, int position) {
        Amiibo_ currentItem = mAmiibosList.get(position);

        String imageUrl = currentItem.getImage();
        String amiiboName = currentItem.getCharacter();

        if (mSharedPreferences.getBoolean(currentItem.getHead() + currentItem.getTail(), false)) {
            holder.mPurchaseIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.mPurchaseIndicator.setVisibility(View.INVISIBLE);
        }
        holder.mAmiiboName.setText(amiiboName);
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mAmiiboImageView);
    }

    @Override
    public int getItemCount() {
        return mAmiibosList.size();
    }

    public class AmiiboViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mAmiiboImageView;
        private TextView mAmiiboName;
        private TextView mPurchaseIndicator;

        public AmiiboViewHolder(@NonNull View itemView) {
            super(itemView);
            mAmiiboImageView = itemView.findViewById(R.id.amiibos_image_text_view);
            mAmiiboName = itemView.findViewById(R.id.amiibo_name_text_view);
            mPurchaseIndicator = itemView.findViewById(R.id.purchase_indicator);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            final Amiibo_ currentItem = mAmiibosList.get(itemPosition);

            Intent intent = new Intent(mContext, AmiiboDetailsActivity.class);
            intent.putExtra("amiibo_series", currentItem.getAmiiboSeries());
            intent.putExtra("character", currentItem.getCharacter());
            intent.putExtra("game_series", currentItem.getGameSeries());
            intent.putExtra("head", currentItem.getHead());
            intent.putExtra("tail", currentItem.getTail());
            intent.putExtra("type", currentItem.getType());
            mContext.startActivity(intent);
        }
    }
}
