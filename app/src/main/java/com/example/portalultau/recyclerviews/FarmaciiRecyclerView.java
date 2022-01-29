package com.example.portalultau.recyclerviews;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portalultau.R;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.fragments.Farmacii;

import java.util.ArrayList;
import java.util.List;

public class FarmaciiRecyclerView extends RecyclerView.Adapter<FarmaciiRecyclerView.ViewHolder> {

    private ArrayList<Farmacie> localDataSet;
    private farmaciiRecyclerViewButtonControl buttonControl;

    public interface farmaciiRecyclerViewButtonControl{
        void deleteFarmacie(Farmacie farmacie);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nume, adresa;
        private Button deleteButton;

        public ViewHolder(View view) {
            super(view);

            nume = view.findViewById(R.id.numeItemFarmacie);
            adresa = view.findViewById(R.id.adresaItemFarmacie);
            deleteButton = view.findViewById(R.id.deleteBtnItemFarmacie);
        }
    }

    public FarmaciiRecyclerView(ArrayList<Farmacie> dataSet, Activity activity) {
        localDataSet = dataSet;
        buttonControl = (farmaciiRecyclerViewButtonControl) activity;
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
            buttonControl.deleteFarmacie(localDataSet.get(position));
            localDataSet.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, localDataSet.size());
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

