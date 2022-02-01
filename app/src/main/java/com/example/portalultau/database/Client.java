package com.example.portalultau.database;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Client extends RealmObject {

    @PrimaryKey
    private ObjectId _id = new ObjectId();
    @Required
    private String nume;
    private String prenume;
    @Required
    private String adresa;
    @Required
    private String contact;
    private int varsta;
    private Boolean abonamentPremium;
    private Boolean isExpanded = false;

    public Client(){

    }

    public Client(String nume, String adresa, String contact) {
        this.nume = nume;
        this.adresa = adresa;
        this.contact = contact;
    }

    public Client(String nume, String prenume, String adresa, String contact, int varsta, Boolean abonamentPremium) {
        if ( nume.equals("") || nume == null || nume.length() < 3) throw new IllegalArgumentException("Nume introdus invalid");
        else this.nume = nume;
        if ( prenume.equals("") || prenume == null || prenume.length() < 3) throw new IllegalArgumentException("Prenume introdus invalid");
        else this.prenume = prenume;
        if ( adresa.equals("") || adresa == null || adresa.length() < 3) throw new IllegalArgumentException("Adresa introdusa invalida");
        else this.adresa = adresa;
        if ( contact.equals("") || contact == null || contact.length() < 3) throw new IllegalArgumentException("Contact introdus invalid");
        else this.contact = contact;
        if (varsta <= 0 || varsta > 150 ) throw new IllegalArgumentException("Varsta introdusa invalida");
        else this.varsta = varsta;
        if (abonamentPremium == null) throw new IllegalArgumentException("Va rog sa selectati starea abonamentului premium");
        else this.abonamentPremium = abonamentPremium;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        if ( nume.length() < 3 ) throw new IllegalArgumentException("Numele trebuie sa fie mai mare de 3 caractere");
        else {
            this.nume = nume;
        }
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        if (prenume.length() < 3 ) throw new IllegalArgumentException("Prenumele trebuie sa fie mai mare de 3 caractere");
        else {
            this.prenume = prenume;
        }
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        if(adresa.length() < 3 ) throw new IllegalArgumentException("Adresa trebuie sa fie mai mare de 3 caractere");
        else {
            this.adresa = adresa;
        }
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        if ( validateContact(contact) ) this.contact = contact;
        else throw new IllegalArgumentException("Formatul informatiilor de contact nu este corespunzator");
    }

    private boolean validateContact(String contact) {
        if ( contact.charAt(0) == '0' && contact.length() == 10)
            return true;
        else {
            String[] split = contact.split("@");
            if(split.length != 2) return false;
            if(split[1].split(".").length != 2) return false;
            return true;
        }
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public Boolean getAbonamentPremium() {
        return abonamentPremium;
    }

    public void setAbonamentPremium(Boolean abonamentPremium) {
        this.abonamentPremium = abonamentPremium;
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public void copyClientData(String nume, String prenume, String adresa, String contact, int varsta, Boolean abonamentPremium){
        this.nume = nume;
        this.prenume = prenume;
        this.adresa = adresa;
        this.contact = contact;
        this.varsta = varsta;
        this.abonamentPremium = abonamentPremium;
    }
}
