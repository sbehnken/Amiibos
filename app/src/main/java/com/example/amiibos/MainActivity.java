package com.example.amiibos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.amiibos.Models.Amiibo;
import com.example.amiibos.Models.Amiibo_;
import com.example.amiibos.Services.AmiibosService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.amiibos.Statics.DELETE_KEY;
import static com.example.amiibos.Statics.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {

    private AmiiboAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        final int columns = getResources().getInteger(R.integer.gallery_columns);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        adapter = new AmiiboAdapter(this);
        mRecyclerView.setAdapter(adapter);

        getAmiiboResponse();
    }

    private void updateAdapter(List<Amiibo_> amiibos) {
        Set<String> deletedAmiibos = sharedPreferences.getStringSet(DELETE_KEY, new HashSet<>());
        //Filtering out deleted items from response
        List<Amiibo_> filteredAmiibos = new ArrayList<>(amiibos);
        filteredAmiibos.removeIf(
                amiibo_ -> deletedAmiibos.contains(amiibo_.getHead() + amiibo_.getTail()));
        adapter.setAmiibos(filteredAmiibos);
        adapter.notifyDataSetChanged();
    }

    private void getAmiiboResponse() {
        AmiibosService amiibosService = new AmiibosService();
        amiibosService.getAllAmiibos().enqueue(new Callback<Amiibo>() {
            @Override
            public void onResponse(@NonNull Call<Amiibo> call, @NonNull Response<Amiibo> response) {
                if (response.body() == null) {
                    Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                } else {
                    updateAdapter(response.body().getAmiibo());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Amiibo> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapter(adapter.getAmiibos());
    }
}