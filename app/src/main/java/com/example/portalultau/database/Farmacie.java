package com.example.portalultau.database;

import androidx.annotation.NonNull;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Farmacie extends RealmObject {

    public Farmacie(){

    }

    public Farmacie(String nume, String adresa) {
        if (!validateText(nume)) throw new IllegalArgumentException("Numele farmaciei trebuie sa fie de minim 3 caractere");
        else {
            this.nume = nume;
        }
        if (!validateText(adresa) ) throw new IllegalArgumentException("Adresa farmaciei trebuie sa fie compusa din minim 3 caractere");
        else{
            this.adresa = adresa;
        }
    }

    public Farmacie(String nume, String adresa, Boolean oferaPreparate, Boolean medicamenteNaturiste) {
        if (!validateText(nume)) throw new IllegalArgumentException("Numele farmaciei trebuie sa fie de minim 3 caractere");
        else {
            this.nume = nume;
        }
        if (!validateText(adresa)) throw new IllegalArgumentException("Adresa farmaciei trebuie sa fie compusa din minim 3 caractere");
        else{
            this.adresa = adresa;
        }
        this.oferaPreparate = oferaPreparate;
        this.medicamenteNaturiste = medicamenteNaturiste;
    }



    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        if (validateText(nume)) throw new IllegalArgumentException("Numele farmaciei trebuie sa fie de minim 3 caractere");
        else {
            this.nume = nume;
        }
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        if ( validateText(adresa) ) throw new IllegalArgumentException("Adresa farmaciei trebuie sa fie compusa din minim 3 caractere");
        else{
            this.adresa = adresa;
        }
    }

    public Boolean getOferaPreparate() {
        return oferaPreparate;
    }

    public void setOferaPreparate(Boolean oferaPreparate) {
        this.oferaPreparate = oferaPreparate;
    }

    public Boolean getMedicamenteNaturiste() {
        return medicamenteNaturiste;
    }

    public void setMedicamenteNaturiste(Boolean medicamenteNaturiste) {
        this.medicamenteNaturiste = medicamenteNaturiste;
    }

    @PrimaryKey private ObjectId _id = new ObjectId();
    @Required private String nume;
    @Required private String adresa;
    private Boolean oferaPreparate;
    private Boolean medicamenteNaturiste;

    private boolean validateText(String text){
        if ( text != null )
            return text.length() > 2;
        return false;
    }

    public void copyFarmacieData(String nume, String adresa, Boolean oferaPreparate, Boolean medicamenteNaturiste){
        this.nume = nume;
        this.adresa = adresa;
        this.oferaPreparate = oferaPreparate;
        this.medicamenteNaturiste = medicamenteNaturiste;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nume;
    }
}
