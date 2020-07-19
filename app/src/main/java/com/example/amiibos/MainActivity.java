package com.example.amiibos;

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

    private void getAmiiboResponse() {
        AmiibosService amiibosService = new AmiibosService();
        amiibosService.getAllAmiibos().enqueue(new Callback<Amiibo>() {
            @Override
            public void onResponse(Call<Amiibo> call, Response<Amiibo> response) {
                if (response.code() == 400 || response.code() == 403) {
                    Toast.makeText(MainActivity.this, "404 Site not found", Toast.LENGTH_SHORT).show();
                } else if (response.body() != null) {
                    Set<String> deletedAmiibos = sharedPreferences.getStringSet(DELETE_KEY, new HashSet<>());
                    //Filtering out deleted items from response
                    List<Amiibo_> filteredAmiibos = new ArrayList<>(response.body().getAmiibo());
                    filteredAmiibos.removeIf(
                            amiibo_ -> deletedAmiibos.contains(amiibo_.getHead() + amiibo_.getTail()));
                    adapter.setAmiibos(filteredAmiibos);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Amiibo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Set<String> deletedAmiibo = sharedPreferences.getStringSet(DELETE_KEY, new HashSet<>());
        List<Amiibo_> filteredAmiibos = new ArrayList<>(adapter.getAmiibos());
        filteredAmiibos.removeIf(
                (Amiibo_ amiibo) -> deletedAmiibo.contains(amiibo.getHead() + amiibo.getTail()));
        adapter.setAmiibos(filteredAmiibos);
        adapter.notifyDataSetChanged();
    }
}