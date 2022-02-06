package com.example.portalultau.recyclerviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.fragments.Clienti;

import java.util.ArrayList;

public class ClientiRecyclerView extends RecyclerView.Adapter<ClientiRecyclerView.ViewHolder>{

    private ArrayList<Client> localDataSet;
    private ArrayList<Client> fullDataSet;
    private clientiRecyclerViewButtonControl buttonControl;
    private Clienti.crudClientiOperations crudOp;
    private Activity activity;

    public interface clientiRecyclerViewButtonControl{
        void editClient(Client client);
        void updateExpand(Client client);
        void stergeTranzactiiPtClient(Client client);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nume, prenume, adresa, contact, varsta, abonamentPremium, expandLabel;
        private LinearLayout hiddenLayout, displayedLayout;
        private Button deleteButton, editButton;

        public ViewHolder(View view) {
            super(view);

            nume = view.findViewById(R.id.numeClientItem);
            prenume = view.findViewById(R.id.prenumeClientItem);
            adresa = view.findViewById(R.id.adresaClientItem);
            contact = view.findViewById(R.id.contactClientItem);
            varsta = view.findViewById(R.id.varstaClientItem);
            abonamentPremium = view.findViewById(R.id.premiumClientItem);
            expandLabel = view.findViewById(R.id.extindeLabelClientItem);
            hiddenLayout = view.findViewById(R.id.hiddenLayoutClientItem);
            displayedLayout = view.findViewById(R.id.displayedLayoutClientItem);
            deleteButton = view.findViewById(R.id.deleteBtnItemClient);
            editButton = view.findViewById(R.id.editBtnItemClient);
        }

        private void setExpanded(Client client){
            hiddenLayout.setVisibility(client.getExpanded() ? View.VISIBLE : View.GONE);
            expandLabel.setVisibility(client.getExpanded() ? View.GONE : View.VISIBLE);
        }
    }

    public ClientiRecyclerView(ArrayList<Client> dataSet, Activity activity) {
        localDataSet = dataSet;
        fullDataSet = new ArrayList<>(dataSet);
        buttonControl = (clientiRecyclerViewButtonControl) activity;
        crudOp = (Clienti.crudClientiOperations) activity;
        this.activity = activity;
    }

    @Override
    public ClientiRecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.client_item, viewGroup, false);

        return new ClientiRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientiRecyclerView.ViewHolder viewHolder, final int position) {
        viewHolder.nume.setText(localDataSet.get(position).getNume());
        viewHolder.prenume.setText(localDataSet.get(position).getPrenume());
        viewHolder.adresa.setText(localDataSet.get(position).getAdresa());
        viewHolder.contact.setText(localDataSet.get(position).getContact());
        viewHolder.varsta.setText(String.valueOf(localDataSet.get(position).getVarsta()));
        if (localDataSet.get(position).getAbonamentPremium()) {
            viewHolder.abonamentPremium.setText("activ");
            viewHolder.abonamentPremium.setTextColor(Color.parseColor("#f1f1f1"));
        } else {
            viewHolder.abonamentPremium.setText("inactiv");
            viewHolder.abonamentPremium.setTextColor(Color.parseColor("#ffbb00"));
        }

        viewHolder.setExpanded(localDataSet.get(position));

        viewHolder.displayedLayout.setOnClickListener(view -> {
            Boolean expanded = localDataSet.get(position).getExpanded();
            buttonControl.updateExpand(localDataSet.get(position));
            notifyItemChanged(position);
        });

        viewHolder.deleteButton.setOnClickListener(view -> {
            createDialogForTranzactii(localDataSet.get(position), position);
        });

        viewHolder.editButton.setOnClickListener(view -> {
            buttonControl.editClient(localDataSet.get(position));
        });

    }

    private void createDialogForTranzactii(Client client, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("Sterge tranzactiile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buttonControl.stergeTranzactiiPtClient(client);
                fullDataSet.remove(localDataSet.get(position));
                crudOp.deleteClient(localDataSet.get(position));
                localDataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, localDataSet.size());
            }
        });
        builder.setNegativeButton("Pastreaza tranzactiile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fullDataSet.remove(localDataSet.get(position));
                crudOp.deleteClient(localDataSet.get(position));
                localDataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, localDataSet.size());
            }
        });

        builder.setNeutralButton("Anuleaza", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setMessage("Doriti sa stergeti si tranzactiile asociate clientului " + client.getNume() + " " + client.getPrenume() + "?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void filter(String charSequence){
        ArrayList<Client> filteredDataSet = new ArrayList<>();

        if ( charSequence == null || charSequence.length() == 0 ){
            filteredDataSet.addAll(fullDataSet);
            Log.d("added", "full set");
        } else {
            String filterPattern = charSequence.toString().toLowerCase().trim();

            for ( Client client : fullDataSet ){
                if (client.getNume().toLowerCase().contains(filterPattern))
                {
                    filteredDataSet.add(client);
                    Log.d("added", client.getNume());
                }
            }
        }

        localDataSet.clear();
        localDataSet.addAll(filteredDataSet);
        notifyDataSetChanged();
    }

    public void updateDataSet(ArrayList<Client> dataSet){
        fullDataSet.clear();
        fullDataSet.addAll(dataSet);
        localDataSet.clear();
        localDataSet.addAll(fullDataSet);
        notifyDataSetChanged();
    }
}
