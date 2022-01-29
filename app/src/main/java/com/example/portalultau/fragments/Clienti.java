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

import com.example.portalultau.R;

/*
    @author Sandu Dragos
 */
public class Clienti extends Fragment {

    private ImageButton addClient;
    private NavController navController;
    private SearchView searchView;
    private TextView heroLabel;

    public Clienti() {
        // Required empty public constructor
    }

    public static Clienti newInstance() {
        Clienti fragment = new Clienti();
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
        View view = inflater.inflate(R.layout.fragment_clienti, container, false);

        searchView = view.findViewById(R.id.searchClienti);
        heroLabel = view.findViewById(R.id.clientiHeroText);
        addClient = view.findViewById(R.id.addClient);
        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        addClient.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }
}