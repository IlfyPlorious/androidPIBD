package com.example.portalultau.helpers;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Produs {

    private String nume;
    private Float pret;

    public Produs(){

    }

    public Produs(String nume, float pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        if(nume.equals("") || nume == null || nume.length() < 3) throw new IllegalArgumentException();
        else this.nume = nume;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(Float pret) {
        this.pret = pret;
    }

    @NonNull
    @Override
    public String toString() {
        NumberFormat format = new DecimalFormat("0.00");
        String pret = format.format(this.pret);
        return this.nume + ", pret " + pret + " lei";
    }
}
