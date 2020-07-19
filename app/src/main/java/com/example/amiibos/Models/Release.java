package com.example.amiibos.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Release {

    @SerializedName("au")
    @Expose
    private String au;
    @SerializedName("eu")
    @Expose
    private String eu;
    @SerializedName("jp")
    @Expose
    private String jp;
    @SerializedName("na")
    @Expose
    private String na;

    public String getAu() {
        return au;
    }

    public void setAu(String au) {
        this.au = au;
    }

    public String getEu() {
        return eu;
    }

    public void setEu(String eu) {
        this.eu = eu;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

}
