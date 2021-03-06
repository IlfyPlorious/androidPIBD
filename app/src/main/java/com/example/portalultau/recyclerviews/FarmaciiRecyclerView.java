package com.example.portalultau.recyclerviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.fragments.Farmacii;
import com.example.portalultau.fragments.additional.AddFarmacie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FarmaciiRecyclerView extends RecyclerView.Adapter<FarmaciiRecyclerView.ViewHolder> {

    private ArrayList<Farmacie> localDataSet;
    private ArrayList<Farmacie> fullDataSet;
    private farmaciiRecyclerViewButtonControl buttonControl;
    private Farmacii.onCRUDFarmacieOperation crudOp;
    private Activity activity;

    public interface farmaciiRecyclerViewButtonControl{
        void editFarmacie(Farmacie farmacie);
        void stergeTranzactiePtFarmacie(Farmacie farmacie);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nume, adresa, preparate, naturiste;
        private Button deleteButton, editButton;

        public ViewHolder(View view) {
            super(view);

            nume = view.findViewById(R.id.numeItemFarmacie);
            adresa = view.findViewById(R.id.adresaItemFarmacie);
            deleteButton = view.findViewById(R.id.deleteBtnItemFarmacie);
            editButton = view.findViewById(R.id.editBtnItemFarmacie);
            preparate = view.findViewById(R.id.preparatFarmacieItem);
            naturiste = view.findViewById(R.id.naturisteFarmacieItem);
        }
    }

    public FarmaciiRecyclerView(ArrayList<Farmacie> dataSet, Activity activity) {
        localDataSet = dataSet;
        fullDataSet = new ArrayList<>(dataSet);
        buttonControl = (farmaciiRecyclerViewButtonControl) activity;
        crudOp = (Farmacii.onCRUDFarmacieOperation) activity;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.farmacie_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.nume.setText(localDataSet.get(position).getNume());
        viewHolder.adresa.setText(localDataSet.get(position).getAdresa());
        viewHolder.deleteButton.setOnClickListener(view -> {
            createDialogForTranzactii(localDataSet.get(position), position);
        });

        viewHolder.editButton.setOnClickListener(view -> {
            buttonControl.editFarmacie(localDataSet.get(position));
        });

        if (localDataSet.get(position).getOferaPreparate()) {
            viewHolder.preparate.setTextColor(Color.parseColor("#f1f1f1"));
            viewHolder.preparate.setText("Ofera preparate");
        }
        else {
            viewHolder.preparate.setTextColor(Color.parseColor("#ffbb00"));
            viewHolder.preparate.setText("Nu ofera preparate");
        }

        if (localDataSet.get(position).getMedicamenteNaturiste()) {
            viewHolder.naturiste.setTextColor(Color.parseColor("#f1f1f1"));
            viewHolder.naturiste.setText("Ofera medicamente naturiste");
        }
        else {
            viewHolder.naturiste.setTextColor(Color.parseColor("#ffbb00"));
            viewHolder.naturiste.setText("Nu ofera medicamente naturiste");
        }
    }

    private void createDialogForTranzactii(Farmacie farmacie, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("Sterge tranzactiile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buttonControl.stergeTranzactiePtFarmacie(farmacie);
                fullDataSet.remove(localDataSet.get(position));
                crudOp.deleteFarmacie(localDataSet.get(position));
                localDataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, localDataSet.size());
            }
        });
        builder.setNegativeButton("Pastreaza tranzactiile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fullDataSet.remove(localDataSet.get(position));
                crudOp.deleteFarmacie(localDataSet.get(position));
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

        builder.setMessage("Doriti sa stergeti si tranzactiile asociate farmaciei " + farmacie.getNume());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void filter(String charSequence){
        ArrayList<Farmacie> filteredDataSet = new ArrayList<>();

        if ( charSequence == null || charSequence.length() == 0 ){
            filteredDataSet.addAll(fullDataSet);
            Log.d("added", "full set");
        } else {
            String filterPattern = charSequence.toString().toLowerCase().trim();

            for ( Farmacie farmacie : fullDataSet ){
                if (farmacie.getNume().toLowerCase().contains(filterPattern))
                {
                    filteredDataSet.add(farmacie);
                    Log.d("added", farmacie.getNume());
                }
            }
        }

        localDataSet.clear();
        localDataSet.addAll(filteredDataSet);
        notifyDataSetChanged();
    }

    public void updateDataSet(ArrayList<Farmacie> dataSet){
        fullDataSet.clear();
        fullDataSet.addAll(dataSet);
        localDataSet.clear();
        localDataSet.addAll(fullDataSet);
        notifyDataSetChanged();
    }
}

