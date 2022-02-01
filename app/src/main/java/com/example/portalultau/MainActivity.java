package com.example.portalultau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.portalultau.database.Client;
import com.example.portalultau.fragments.Clienti;
import com.example.portalultau.fragments.Farmacii;
import com.example.portalultau.fragments.additional.AddFarmacie;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.recyclerviews.ClientiRecyclerView;
import com.example.portalultau.recyclerviews.FarmaciiRecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

/*
    @author Sandu Dragos
 */

public class MainActivity extends AppCompatActivity implements Farmacii.onCRUDFarmacieOperation, Farmacii.farmaciiRealmComm, FarmaciiRecyclerView.farmaciiRecyclerViewButtonControl, ClientiRecyclerView.clientiRecyclerViewButtonControl, Clienti.clientiRealmComm, Clienti.crudClientiOperations {

    private BottomNavigationView bottomNav;
    private NavController navController;
    private RealmConfiguration config;
    private Realm backgroundThreadRealm;
    private SyncConfiguration syncConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///-------------- database initialization ------------///
        Realm.init(this);
        App app = new App(new AppConfiguration.Builder("portalultau-btvfp").build());
        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("Login", "Successfully authenticated anonymously.");
                User user = app.currentUser();
                String partitionValue = "PortalulTau";
                syncConfig = new SyncConfiguration.Builder(
                        user,
                        partitionValue)
                        .allowWritesOnUiThread(true)
                        .allowQueriesOnUiThread(true)
                        .build();
                backgroundThreadRealm = Realm.getInstance(syncConfig);
            } else {
                Log.e("Login", "Failed to log in. Error: " + result.getError());
            }
        });

        //        String realmName = "Database";
//        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
//        Realm backgroundThreadRealm = Realm.getInstance(config);
//        //Realm.deleteRealm(config);

        ///------------ bottom navigation -----------///

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setItemIconTintList(null);

        navController = Navigation.findNavController(this, R.id.fragmentContainer);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public void insertFarmacie(Farmacie farmacie) {
        try{
            backgroundThreadRealm.executeTransaction(transactionRealm -> {
                transactionRealm.insert(farmacie);
            });
        } catch (Exception e){
            Toast.makeText(this, "Eroare la incarcare in baza de date: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateFarmacie(Farmacie farmacie) {
       try{
           backgroundThreadRealm.executeTransaction(transaction -> {
               Farmacie farmacieToEdit = transaction.where(Farmacie.class).equalTo("_id", farmacie.getId()).findFirst();
               if ( farmacieToEdit != null )
                   farmacieToEdit.copyFarmacieData(farmacie.getNume(), farmacie.getAdresa(), farmacie.getOferaPreparate(), farmacie.getMedicamenteNaturiste());
           });
       } catch (Exception e){
           Toast.makeText(this, "Eroare la baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public ArrayList<Farmacie> citesteFarmacii() {
        // all tasks in the realm
        RealmResults<Farmacie> farmacii = backgroundThreadRealm.where(Farmacie.class).findAll();
        return new ArrayList<>(farmacii);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundThreadRealm.close();
    }

    @Override
    public void deleteFarmacie(Farmacie farmacie) {
        ObjectId farmacieId = farmacie.getId();
        try{
            backgroundThreadRealm.executeTransaction(transactionRealm -> {
                Farmacie toDelete = transactionRealm.where(Farmacie.class).equalTo("_id", farmacieId).findFirst();
                if (toDelete != null)
                    toDelete.deleteFromRealm();
            });
            Toast.makeText(this, "Farmacia a fost stearsa", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "Eroare la baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editFarmacie(Farmacie farmacie) {
        Bundle arguments = new Bundle();
        arguments.putString("id", farmacie.getId().toString());
        arguments.putString("nume", farmacie.getNume());
        arguments.putString("adresa", farmacie.getAdresa());
        arguments.putBoolean("preparate", farmacie.getOferaPreparate());
        arguments.putBoolean("naturiste", farmacie.getMedicamenteNaturiste());
        navController.navigate(R.id.action_farmaciiFrag_to_editFarmacie, arguments);
    }

    @Override
    public void insertClient(Client client) {
        try{
            backgroundThreadRealm.executeTransaction(transactionRealm -> {
                transactionRealm.insert(client);
            });
        } catch (Exception e){
            Toast.makeText(this, "Eroare la incarcare in baza de date: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateClient(Client client) {
        try{
            backgroundThreadRealm.executeTransaction(transaction -> {
                Client clientToEdit = transaction.where(Client.class).equalTo("_id", client.get_id()).findFirst();
                if ( clientToEdit != null )
                    clientToEdit.copyClientData(client.getNume(), client.getPrenume(), client.getAdresa(), client.getContact(), client.getVarsta(), client.getAbonamentPremium());
            });
        } catch (Exception e){
            Toast.makeText(this, "Eroare la baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteClient(Client client) {
        ObjectId clientId = client.get_id();
        try{
            backgroundThreadRealm.executeTransaction(transactionRealm -> {
                Client toDelete = transactionRealm.where(Client.class).equalTo("_id", clientId).findFirst();
                if (toDelete != null)
                    toDelete.deleteFromRealm();
            });
            Toast.makeText(this, "Clientul a fost sters", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "Eroare la baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editClient(Client client) {
        Bundle arguments = new Bundle();
        arguments.putString("id", client.get_id().toString());
        arguments.putString("nume", client.getNume());
        arguments.putString("prenume", client.getPrenume());
        arguments.putString("adresa", client.getAdresa());
        arguments.putString("contact", client.getContact());
        arguments.putBoolean("premium", client.getAbonamentPremium());
        arguments.putInt("varsta", client.getVarsta());
        navController.navigate(R.id.action_clientiFrag_to_editClient, arguments);
    }

    @Override
    public void updateExpand(Client client) {
        Boolean expanded = client.getExpanded();
        try{
            backgroundThreadRealm.executeTransaction(transaction -> {
                Client clientToEdit = transaction.where(Client.class).equalTo("_id", client.get_id()).findFirst();
                clientToEdit.setExpanded(!expanded);
            });
        } catch (Exception e){
            Toast.makeText(this, "Eroare la baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ArrayList<Client> citesteClienti() {
        RealmResults<Client> clienti = backgroundThreadRealm.where(Client.class).findAll();
        return new ArrayList<>(clienti);
    }
}