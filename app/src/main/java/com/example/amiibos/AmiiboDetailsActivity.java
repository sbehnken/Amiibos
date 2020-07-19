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

    private SharedPreferences mSharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amiibo_details);

        mSharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        final Button mRemovePurchase = findViewById(R.id.unpurchase_button);
        final Button mPurchase = findViewById(R.id.purchase_button);

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


        if (mSharedPreferences.getBoolean(head + tail, false)) {
            mRemovePurchase.setVisibility(View.VISIBLE);
            mPurchase.setVisibility(View.INVISIBLE);
        } else {
            mRemovePurchase.setVisibility(View.INVISIBLE);
            mPurchase.setVisibility(View.VISIBLE);
        }

        mRemovePurchase.setOnClickListener(v -> {
            mRemovePurchase.setVisibility(View.INVISIBLE);
            mPurchase.setVisibility(View.VISIBLE);
            setAddToList(false);
            Toast.makeText(getBaseContext(), "Removed from Purchases", Toast.LENGTH_SHORT).show();
        });

        mPurchase.setOnClickListener(v -> {
            mRemovePurchase.setVisibility(View.VISIBLE);
            mPurchase.setVisibility(View.INVISIBLE);
            setAddToList(true);
            Toast.makeText(getBaseContext(), "Purchased", Toast.LENGTH_SHORT).show();

            final Set<String> purchasedAmiibos = mSharedPreferences.getStringSet("purchased", new HashSet<>());
            Set<String> copy = new HashSet<>(purchasedAmiibos);
            copy.add(head + tail);
            mSharedPreferences.edit().putStringSet("purchased", copy).apply();
        });

        deleteAmiibo.setOnClickListener(view -> {
            final Set<String> deletedAmiibo = mSharedPreferences.getStringSet("deleted", new HashSet<>());
            Set<String> copy = new HashSet<>(deletedAmiibo);

            //different way of doing it
//                Set<String> copy = new HashSet<>(mSharedPreferences.getStringSet("deleted", new HashSet<>()));

            copy.add(head + tail);
            mSharedPreferences.edit().putStringSet("deleted", copy).apply();

            Log.d("unicorn", "Shared Pref updated with Set: " + copy);

            onBackPressed();
        });
    }

    public void setAddToList(boolean value) {
        Log.d("unicorn", "Purchase saved to SharedPrefs");
        mSharedPreferences.edit().putBoolean(head + tail, value).apply();
    }
}
