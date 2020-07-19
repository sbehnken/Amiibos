package com.example.amiibos.Services;

import com.example.amiibos.Models.Amiibo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AmiibosInterface {

    @GET("amiibo/")
    Call<Amiibo> getAllAmiibo();

}