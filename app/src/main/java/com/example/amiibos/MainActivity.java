package com.example.amiibos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.amiibos.Models.Amiibo;
import com.example.amiibos.Models.Amiibo_;
import com.example.amiibos.Services.AmiibosService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.amiibos.AmiiboAdapter.SHARED_PREFS;

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
        amiibosService.getAllAmiibo().enqueue(new Callback<Amiibo>() {
            @Override
            public void onResponse(Call<Amiibo> call, Response<Amiibo> response) {
                if (response.code() == 400 || response.code() == 403) {
                    Toast.makeText(MainActivity.this, "404 Site not found", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.setAmiibos(response.body().getAmiibo());

                    Set<String> deletedAmiibos = sharedPreferences.getStringSet("deleted", new HashSet<>());
                    Log.d("unicorn", "Deleted Amiibos in Shared Pref: getAmiiboResponse" + deletedAmiibos);
                    adapter.getAmiibos().removeIf(
                            amiibo_ -> deletedAmiibos.contains(amiibo_.getHead() + amiibo_.getTail()));

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
        final Set<String> deletedAmiibo = sharedPreferences.getStringSet("deleted", new HashSet<>());
        Log.d("unicorn", "onResume - Deleted Amiibos in Shared Pref: " + deletedAmiibo);
        adapter.getAmiibos().removeIf(
                (Amiibo_ amiibo) -> {
                    return deletedAmiibo.contains(amiibo.getHead() + amiibo.getTail());
                });

        adapter.notifyDataSetChanged();
    }
}