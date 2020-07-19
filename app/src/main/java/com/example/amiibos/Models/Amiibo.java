package com.example.amiibos.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Amiibo {

    @SerializedName("amiibo")
    @Expose
    private List<Amiibo_> amiibo = null;

    public List<Amiibo_> getAmiibo() {
        return amiibo;
    }

    public void setAmiibo(List<Amiibo_> amiibo) {
        this.amiibo = amiibo;
    }

}
