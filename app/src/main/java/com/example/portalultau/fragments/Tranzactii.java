package com.example.portalultau.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.database.Tranzactie;
import com.example.portalultau.recyclerviews.TranzactiiRecyclerView;

import java.util.ArrayList;

/*
    @author Sandu Dragos
 */


public class Tranzactii extends Fragment {

    private ImageButton addTranzactie, reloadBtn;
    private NavController navController;
    private SearchView searchView;
    private TextView heroLabel;
    private Spinner filterSpinner;
    private RecyclerView recyclerView;

    private realmCommTranzactii realmComm;
    private Clienti.clientiRealmComm realmCommClienti;
    private Farmacii.farmaciiRealmComm realmCommFarmacii;

    private ArrayList<Client> listaClienti = new ArrayList<>();
    private ArrayList<Farmacie> listaFarmacie = new ArrayList<>();
    private ArrayList<Tranzactie> listaTranzactie = new ArrayList<>();

    public Tranzactii() {
        // Required empty public constructor
    }

    public static Tranzactii newInstance() {
        Tranzactii fragment = new Tranzactii();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tranzactii, container, false);

        searchView = view.findViewById(R.id.searchTranzactii);
        heroLabel = view.findViewById(R.id.tranzactiiHeroText);
        addTranzactie = view.findViewById(R.id.addTranzactii);
        filterSpinner = view.findViewById(R.id.filterSpinner);
        recyclerView = view.findViewById(R.id.tranzactiiRecycler);
        reloadBtn = view.findViewById(R.id.reloadTranzactiiButton);

        realmComm = (realmCommTranzactii) getActivity();
        realmCommClienti = (Clienti.clientiRealmComm) getActivity();
        realmCommFarmacii = (Farmacii.farmaciiRealmComm) getActivity();

        try{
            listaClienti = realmCommClienti.citesteClienti();
            listaFarmacie = realmCommFarmacii.citesteFarmacii();
            listaTranzactie = realmComm.citesteTranzactii();
        } catch (Exception e){
            Toast.makeText(getContext(), "Eroare la citirea din baza de date" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        addTranzactie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_tranzactiiFrag_to_addTranzactie);
            }
        });

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heroLabel.setVisibility(View.INVISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                heroLabel.setVisibility(View.VISIBLE);
                return false;
            }
        });


        ArrayList<String> filterChoices = new ArrayList<>();
        filterChoices.add("client");
        filterChoices.add("farmacie");

        ArrayAdapter<?> filterAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, filterChoices);
        filterAdapter.setDropDownViewResource(R.layout.spinner_item);
        filterSpinner.setAdapter(filterAdapter);

        //setup recycler

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TranzactiiRecyclerView adapter = new TranzactiiRecyclerView(listaTranzactie, listaFarmacie, listaClienti, getActivity());
        recyclerView.setAdapter(adapter);

        reloadBtn.setOnClickListener(view13 -> {
            listaTranzactie = realmComm.citesteTranzactii();
            adapter.updateDataSet(listaTranzactie);
            Toast.makeText(getContext(), "Lista de tranzactii a fost actualizata", Toast.LENGTH_SHORT).show();
            if (adapter.getItemCount() - 1 > 0)
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText, filterSpinner.getSelectedItem().toString());
                return false;
            }
        });

        return view;
    }

    public interface crudTranzactiiOperations{
        void insertTranzactie(Tranzactie tranzactie);
        void updateTranzactie(Tranzactie tranzactie);
        void deleteTranzactie(Tranzactie tranzactie);
    }

    public interface realmCommTranzactii{
        ArrayList<Tranzactie> citesteTranzactii();
    }
}