package com.example.amiibos.Services;

import com.example.amiibos.Models.Amiibo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AmiibosService {

    private AmiibosInterface amiibosInterface;

    public AmiibosService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.amiiboapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        amiibosInterface = retrofit.create(AmiibosInterface.class);
    }

    public Call<Amiibo> getAllAmiibos() {
        return amiibosInterface.getAllAmiibos();
    }
}
