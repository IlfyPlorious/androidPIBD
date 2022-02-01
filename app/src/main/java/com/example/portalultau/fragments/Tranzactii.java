package com.example.portalultau.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portalultau.R;

/*
    @author Sandu Dragos
 */


public class Tranzactii extends Fragment {

    private ImageButton addTranzactie, reloadBtn;
    private NavController navController;
    private SearchView searchView;
    private TextView heroLabel;

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
        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        addTranzactie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.action_farmaciiFrag_to_addFarmacie);
            }
        });

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

//        reloadBtn.setOnClickListener(view13 -> {
//            tranzactiiList = comm.citesteTranzactii();
//            adapter.updateDataSet(tranzactiiList);
//            Toast.makeText(getContext(), "Lista de tranzactii a fost actualizata", Toast.LENGTH_SHORT).show();
//            if (adapter.getItemCount() - 1 > 0)
//                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
//        });

        return view;
    }
}