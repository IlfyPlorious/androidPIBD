package com.example.portalultau.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portalultau.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Farmacii#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Farmacii extends Fragment {

    private ImageButton addFarmacie;
    private NavController navController;
    private SearchView searchView;
    private TextView heroLabel;

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

        searchView = view.findViewById(R.id.searchFarmacii);
        heroLabel = view.findViewById(R.id.farmacieHeroText);
        addFarmacie = view.findViewById(R.id.addFarmacie);
        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        addFarmacie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_farmaciiFrag_to_addFarmacie);
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