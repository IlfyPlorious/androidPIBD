package com.example.portalultau.recyclerviews;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.database.Tranzactie;
import com.example.portalultau.fragments.Farmacii;
import com.example.portalultau.fragments.Tranzactii;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TranzactiiRecyclerView extends RecyclerView.Adapter<TranzactiiRecyclerView.ViewHolder> {

    private ArrayList<Tranzactie> localDataSet;
    private ArrayList<Tranzactie> fullDataSet;
    private List<Farmacie> listaFarmacii;
    private List<Client> listaClienti;
    private Tranzactii.crudTranzactiiOperations crudTranzactiiOperations;

    public TranzactiiRecyclerView(ArrayList<Tranzactie> dataSet, ArrayList<Farmacie> listaFarmacii, ArrayList<Client> listaClienti, Activity activity){
        localDataSet = dataSet;
        fullDataSet = new ArrayList<>(dataSet);
        this.listaClienti = listaClienti;
        this.listaFarmacii = listaFarmacii;
        crudTranzactiiOperations = (Tranzactii.crudTranzactiiOperations) activity;
    }

    @NonNull
    @Override
    public TranzactiiRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tranzactii_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Client client = getClientForTranzactie(localDataSet.get(position));
        Farmacie farmacie = getFarmacieForTranzactie(localDataSet.get(position));

        holder.id.setText("Tranzactie nr." + localDataSet.get(position).get_id());
        holder.data.setText("Data: " + localDataSet.get(position).getData());
        holder.client.setText("Client: " + client.getNume() + " " + client.getPrenume());
        holder.farmacie.setText("Farmacie: " + farmacie.getNume());

        //button functions
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int holderPosition = holder.getAdapterPosition();
                fullDataSet.remove(localDataSet.get(holderPosition));
                crudTranzactiiOperations.deleteTranzactie(localDataSet.get(holderPosition));
                localDataSet.remove(holderPosition);
                notifyItemRemoved(holderPosition);
                notifyItemRangeChanged(holderPosition, localDataSet.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private Button vizualizeaza, delete;
        private TextView id, client, farmacie, data;

        public ViewHolder(View view) {
            super(view);

            vizualizeaza = view.findViewById(R.id.vizualizeazaBtnTranzactii);
            delete = view.findViewById(R.id.deleteBtnTranzactii);
            id = view.findViewById(R.id.nrTranzactieItem);
            client = view.findViewById(R.id.clientTranzactieItem);
            farmacie = view.findViewById(R.id.farmacieTranzactieItem);
            data = view.findViewById(R.id.dataTranzactieItem);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filter(String charSequence, String filterChoice){
        ArrayList<Tranzactie> filteredDataSet = new ArrayList<>();

        if ( charSequence == null || charSequence.length() == 0 ){
            filteredDataSet.addAll(fullDataSet);
            Log.d("added", "full set");
        } else {
            String filterPattern = charSequence.toString().toLowerCase().trim();

            if (filterChoice.equals("client")){
                for ( Tranzactie tranzactie : fullDataSet ){
                    Client client = getClientForTranzactie(tranzactie);

                    if (client.getNume().toLowerCase().contains(filterPattern))
                    {
                        filteredDataSet.add(tranzactie);
                        Log.d("added", tranzactie.get_id().toString());
                    }
                }
            } else if (filterChoice.equals("farmacie")){
                for ( Tranzactie tranzactie : fullDataSet ){
                    Farmacie farmacie = getFarmacieForTranzactie(tranzactie);

                    if (farmacie.getNume().toLowerCase().contains(filterPattern))
                    {
                        filteredDataSet.add(tranzactie);
                        Log.d("added", tranzactie.get_id().toString());
                    }
                }
            }
        }

        localDataSet.clear();
        localDataSet.addAll(filteredDataSet);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Client getClientForTranzactie(Tranzactie tranzactie){
        Client client;

        List<Client> getClientList;
        getClientList = listaClienti.stream().filter(client1 -> client1.get_id().equals(tranzactie.getIdClient()))
                .collect(Collectors.toList());

        client = getClientList.get(0);

        return client;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Farmacie getFarmacieForTranzactie(Tranzactie tranzactie){
        Farmacie farmacie;

        List<Farmacie> getFarmacieList;
        getFarmacieList = listaFarmacii.stream().filter(farmacie1 -> farmacie1.getId().equals(tranzactie.getIdFarmacie()))
                .collect(Collectors.toList());

        farmacie = getFarmacieList.get(0);

        return farmacie;
    }

    public void updateDataSet(ArrayList<Tranzactie> dataSet){
        fullDataSet.clear();
        fullDataSet.addAll(dataSet);
        localDataSet.clear();
        localDataSet.addAll(fullDataSet);
        notifyDataSetChanged();
    }
}
