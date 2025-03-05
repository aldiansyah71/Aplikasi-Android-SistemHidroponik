package com.example.sistemhidroponik;

public class HistoryItem {
    private String date;
    private String time;
    private String kelarutan;
    private String kelembapan;
    private String pH;
    private String suhuAir;
    private String suhuLingkungan;
    private String pHdown;
    private String pHup;
    private String pompaNutrisi;
    private String waterHeater;
    private String fan;

    public HistoryItem(String date, String time, String kelarutan, String kelembapan, String pH, String suhuAir, String suhuLingkungan, String pHdown, String pHup, String pompaNutrisi, String waterHeater, String fan) {
        this.date = date;
        this.time = time;
        this.kelarutan = kelarutan;
        this.kelembapan = kelembapan;
        this.pH = pH;
        this.suhuAir = suhuAir;
        this.suhuLingkungan = suhuLingkungan;
        this.pHdown = pHdown;
        this.pHup = pHup;
        this.pompaNutrisi = pompaNutrisi;
        this.waterHeater = waterHeater;
        this.fan = fan;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getKelarutan() {
        return kelarutan;
    }

    public String getKelembapan() {
        return kelembapan;
    }

    public String getPH() {
        return pH;
    }

    public String getSuhuAir() {
        return suhuAir;
    }

    public String getSuhuLingkungan() {
        return suhuLingkungan;
    }

    public String getPHdown() {
        return pHdown;
    }

    public String getPHup() {
        return pHup;
    }

    public String getPompaNutrisi() {
        return pompaNutrisi;
    }

    public String getWaterHeater() {
        return waterHeater;
    }

    public String getFan() {
        return fan;
    }
}
