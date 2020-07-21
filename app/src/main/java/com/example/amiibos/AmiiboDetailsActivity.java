package com.example.amiibos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amiibos.Models.Amiibo_;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

import static com.example.amiibos.Statics.AMIIBO_ITEM;
import static com.example.amiibos.Statics.DELETE_KEY;
import static com.example.amiibos.Statics.PURCHASED_KEY;
import static com.example.amiibos.Statics.SHARED_PREFS;

public class AmiiboDetailsActivity extends AppCompatActivity {

    private String head;
    private String tail;
    private Button removePurchase;
    private Button purchase;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amiibo_details);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        removePurchase = findViewById(R.id.unpurchase_button);
        purchase = findViewById(R.id.purchase_button);

        setupUI();

        setClickListeners();
    }

    private void setupUI() {
        TextView amiiboSeriesText = findViewById(R.id.amiibo_series_text_view);
        TextView characterText = findViewById(R.id.amiibo_character_text_view);
        TextView gameSeriesText = findViewById(R.id.amiibo_game_series_view);
        TextView typeText = findViewById(R.id.amiibo_type_text_view);
        ImageView characterImage = findViewById(R.id.amiibo_image_view);

        Gson gson = new Gson();
        String jsonString = getIntent().getStringExtra(AMIIBO_ITEM);
        Amiibo_ amiibo = gson.fromJson(jsonString, Amiibo_.class);
        head = amiibo.getHead();
        tail = amiibo.getTail();

        Picasso.with(this).load(amiibo.getImage()).into(characterImage);
        amiiboSeriesText.setText(getResources().getString(R.string.amiibo_series_label, amiibo.getAmiiboSeries()));
        characterText.setText(getResources().getString(R.string.amiibo_character_label, amiibo.getCharacter()));
        gameSeriesText.setText(getResources().getString(R.string.amiibo_game_series_label, amiibo.getGameSeries()));
        typeText.setText(getResources().getString(R.string.amiibo_type_label, amiibo.getType()));
    }

    private void setClickListeners() {
        Button deleteAmiibo = findViewById(R.id.delete_amiibo);

        Set<String> purchasedAmiibos = sharedPreferences.getStringSet(PURCHASED_KEY, new HashSet<>());
        setIsPurchasedVisibility(purchasedAmiibos.contains(head + tail));

        removePurchase.setOnClickListener(v -> {
            setIsPurchasedVisibility(false);
            updateIsPurchased(false);
            Toast.makeText(getBaseContext(), getResources().getString(R.string.remove_from_purchases_toast), Toast.LENGTH_SHORT).show();
        });

        purchase.setOnClickListener(v -> {
            setIsPurchasedVisibility(true);
            updateIsPurchased(true);
            Toast.makeText(getBaseContext(), getResources().getString(R.string.purchase_toast), Toast.LENGTH_SHORT).show();
        });

        deleteAmiibo.setOnClickListener(view -> {
            Set<String> deletedAmiibos = sharedPreferences.getStringSet(DELETE_KEY, new HashSet<>());
            deletedAmiibos.add(head + tail);
            sharedPreferences.edit().putStringSet(DELETE_KEY, deletedAmiibos).apply();

            onBackPressed();
        });
    }

    private void setIsPurchasedVisibility(boolean isPurchased) {
        if (isPurchased) {
            removePurchase.setVisibility(View.VISIBLE);
            purchase.setVisibility(View.INVISIBLE);
        } else {
            removePurchase.setVisibility(View.INVISIBLE);
            purchase.setVisibility(View.VISIBLE);
        }
    }

    public void updateIsPurchased(boolean value) {
        Set<String> purchasedAmiibos = sharedPreferences.getStringSet(PURCHASED_KEY, new HashSet<>());
        if(value) {
            purchasedAmiibos.add(head + tail);
        } else {
            purchasedAmiibos.remove(head + tail);
        }
        sharedPreferences.edit().putStringSet(PURCHASED_KEY, purchasedAmiibos).apply();
    }
}
