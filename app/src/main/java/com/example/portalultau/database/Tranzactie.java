package com.example.portalultau.database;

import androidx.annotation.NonNull;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Tranzactie extends RealmObject {
    @PrimaryKey
    private ObjectId _id = new ObjectId();
    @Required
    private String data;
    private float suma;
    @Required
    private String produs;
    @Required
    private String tipPlata;
    @Required
    private String cantitateProdus;
    @Required
    private ObjectId idFarmacie;
    @Required
    private ObjectId idClient;
    private Boolean isExpanded = false;

    public Tranzactie(){

    }

    public Tranzactie(String data, float suma, String produs, String tipPlata, String cantitateProdus, ObjectId idFarmacie, ObjectId idClient) {
        if (data != null && !data.equals("")) this.data = data;
        else throw new IllegalArgumentException("Va rog selectati data");
        this.suma = suma;
        this.produs = produs;
        if (tipPlata != null && !tipPlata.equals(""))
            this.tipPlata = tipPlata;
        else throw new IllegalArgumentException("Va rog selectati tipul de plata");
        if (cantitateProdus != null && !cantitateProdus.equals("")) this.cantitateProdus = cantitateProdus;
        else throw new IllegalArgumentException("Va rog sa introduceti cantitatea");
        this.idFarmacie = idFarmacie;
        this.idClient = idClient;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getSuma() {
        return suma;
    }

    public void setSuma(float suma) {
        this.suma = suma;
    }

    public String getProdus() {
        return produs;
    }

    public void setProdus(String produs) {
        this.produs = produs;
    }

    public String getTipPlata() {
        return tipPlata;
    }

    public void setTipPlata(String tipPlata) {
        this.tipPlata = tipPlata;
    }

    public String getCantitateProdus() {
        return cantitateProdus;
    }

    public void setCantitateProdus(String cantitateProdus) {
        this.cantitateProdus = cantitateProdus;
    }

    public ObjectId getIdFarmacie() {
        return idFarmacie;
    }

    public void setIdFarmacie(ObjectId idFarmacie) {
        this.idFarmacie = idFarmacie;
    }

    public ObjectId getIdClient() {
        return idClient;
    }

    public void setIdClient(ObjectId idClient) {
        this.idClient = idClient;
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tranzactie nr. " + this._id.toString();
    }

    public void copyTranzactieData(String data, float suma, String produs, String tipPlata, String cantitateProdus, ObjectId idFarmacie, ObjectId idClient){
        this.data = data;
        this.suma = suma;
        this.produs = produs;
        this.tipPlata = tipPlata;
        this.cantitateProdus = cantitateProdus;
        this.idFarmacie = idFarmacie;
        this.idClient = idClient;
    }
}
