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

    private AmiiboAdapter mAdapter;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        final int columns = getResources().getInteger(R.integer.gallery_columns);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        mAdapter = new AmiiboAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

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
//                    mSharedPreferences.edit().clear().apply();
                    mAdapter.setmAmiibosList((ArrayList) response.body().getAmiibo());

                    final Set<String> deletedAmiibo = mSharedPreferences.getStringSet("deleted", new HashSet<>());
                    Log.d("unicorn", "Deleted Amiibos in Shared Pref: getAmiiboResponse" + deletedAmiibo);
                    mAdapter.getmAmiibosList().removeIf(
                            amiibo_ -> deletedAmiibo.contains(amiibo_.getHead() + amiibo_.getTail()));

                    mAdapter.notifyDataSetChanged();
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
        final Set<String> deletedAmiibo = mSharedPreferences.getStringSet("deleted", new HashSet<>());
        Log.d("unicorn", "onResume - Deleted Amiibos in Shared Pref: " + deletedAmiibo);
        mAdapter.getmAmiibosList().removeIf(
                (Amiibo_ amiibo) -> {
                    return deletedAmiibo.contains(amiibo.getHead() + amiibo.getTail());
                });

        mAdapter.notifyDataSetChanged();
    }
}