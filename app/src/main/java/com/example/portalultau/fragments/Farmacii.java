package com.example.portalultau.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portalultau.R;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.recyclerviews.FarmaciiRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/*
    @author Sandu Dragos
 */

public class Farmacii extends Fragment {

    private ImageButton addFarmacie;
    private NavController navController;
    private SearchView searchView;
    private TextView heroLabel;
    private RecyclerView recyclerView;
    private ArrayList<Farmacie> farmaciiList = new ArrayList<>();
    private farmaciiRealmComm comm;

    public interface farmaciiRealmComm{
        ArrayList<Farmacie> citesteFarmacii();
    }

    public Farmacii() {
        // Required empty public constructor
    }

    public static Farmacii newInstance(String param1, String param2) {
        Farmacii fragment = new Farmacii();
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
        View view = inflater.inflate(R.layout.fragment_farmacii, container, false);

        comm = (farmaciiRealmComm) getActivity();
        searchView = view.findViewById(R.id.searchFarmacii);
        heroLabel = view.findViewById(R.id.farmacieHeroText);
        addFarmacie = view.findViewById(R.id.addFarmacie);

        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        //recycler view setup
        farmaciiList = comm.citesteFarmacii();
        recyclerView = view.findViewById(R.id.farmaciiRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FarmaciiRecyclerView adapter = new FarmaciiRecyclerView(farmaciiList, getActivity());
        recyclerView.setAdapter(adapter);

        addFarmacie.setOnClickListener(view12 -> navController.navigate(R.id.action_farmaciiFrag_to_addFarmacie));

        searchView.setOnSearchClickListener(view1 -> heroLabel.setVisibility(View.INVISIBLE));

        searchView.setOnCloseListener(() -> {
            heroLabel.setVisibility(View.VISIBLE);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }
}