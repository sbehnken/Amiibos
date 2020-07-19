package com.example.amiibos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class AmiiboDetailsActivity extends AppCompatActivity {

    private String head;
    private String tail;

    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amiibo_details);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        final Button removePurchase = findViewById(R.id.unpurchase_button);
        final Button purchase = findViewById(R.id.purchase_button);

        ImageButton deleteAmiibo = findViewById(R.id.delete_amiibo);

        TextView amiiboSeriesTv = findViewById(R.id.amiibo_series_text_view);
        TextView characterTv = findViewById(R.id.amiibo_character_text_view);
        TextView gameSeriesTv = findViewById(R.id.amiibo_game_series_view);
        TextView typeTv = findViewById(R.id.amiibo_type_text_view);

        Intent intent = getIntent();
        String amiiboSeries = intent.getStringExtra("amiibo_series");
        String character = intent.getStringExtra("character");
        String gameSeries = intent.getStringExtra("game_series");
        String type = intent.getStringExtra("type");
        head = intent.getStringExtra("head");
        tail = intent.getStringExtra("tail");

        amiiboSeriesTv.setText("Amiibo Series: " + amiiboSeries);
        characterTv.setText("Character: " + character);
        gameSeriesTv.setText("Game Series: " + gameSeries);
        typeTv.setText("Type: " + type);


        if (sharedPreferences.getBoolean(head + tail, false)) {
            removePurchase.setVisibility(View.VISIBLE);
            purchase.setVisibility(View.INVISIBLE);
        } else {
            removePurchase.setVisibility(View.INVISIBLE);
            purchase.setVisibility(View.VISIBLE);
        }

        removePurchase.setOnClickListener(v -> {
            removePurchase.setVisibility(View.INVISIBLE);
            purchase.setVisibility(View.VISIBLE);
            setAddToList(false);
            Toast.makeText(getBaseContext(), "Removed from Purchases", Toast.LENGTH_SHORT).show();
        });

        purchase.setOnClickListener(v -> {
            removePurchase.setVisibility(View.VISIBLE);
            purchase.setVisibility(View.INVISIBLE);
            setAddToList(true);
            Toast.makeText(getBaseContext(), "Purchased", Toast.LENGTH_SHORT).show();
        });

        deleteAmiibo.setOnClickListener(view -> {
            Set<String> deletedAmiibos = sharedPreferences.getStringSet("deleted", new HashSet<>());
            deletedAmiibos.add(head + tail);
            sharedPreferences.edit().putStringSet("deleted", deletedAmiibos).apply();

            Log.d("unicorn", "Shared Pref updated with Set: " + deletedAmiibos);

            onBackPressed();
        });
    }

    public void setAddToList(boolean value) {
        Log.d("unicorn", "Purchase saved to SharedPrefs");
        sharedPreferences.edit().putBoolean(head + tail, value).apply();
    }
}
