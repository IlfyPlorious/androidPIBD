package com.example.portalultau.database;

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

    public Tranzactie(){

    }

    public Tranzactie(String data, float suma, String produs, String tipPlata, String cantitateProdus, ObjectId idFarmacie, ObjectId idClient) {
        if (validateData(data)) this.data = data;
        else throw new IllegalArgumentException("Format data invalid");
        this.suma = suma;
        if(validateStringBlock(produs))
        this.produs = produs;
        else throw new IllegalArgumentException("Nume produs invalid");
        this.tipPlata = tipPlata;
        this.cantitateProdus = cantitateProdus;
        this.idFarmacie = idFarmacie;
        this.idClient = idClient;
    }

    private boolean validateStringBlock(String block){
        if(block.length() < 3 ) return false;
        else return true;
    }
    private boolean validateData(String data){
        //de modificat in functie de metoda de input

        String[] dataSplit = data.split(".");
        if( dataSplit[0].length() != 2 ) return false;
        return true;
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
}
