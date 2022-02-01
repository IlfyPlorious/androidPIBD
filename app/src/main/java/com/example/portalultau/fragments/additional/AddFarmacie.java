package com.example.portalultau.fragments.additional;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.portalultau.R;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.fragments.Farmacii;

import io.realm.Realm;
import io.realm.mongodb.sync.SyncConfiguration;

/*
    @author Sandu Dragos
 */
public class AddFarmacie extends Fragment {

    private RadioButton prepDa, prepNu, natDa, natNu;
    private EditText nume, adresa;
    private Button adaugaButton;
    private Farmacii.onCRUDFarmacieOperation CRUDFarmacieOperation;
    private String numeToInsert, adresaToInsert;
    private boolean preparate, naturiste;
    private NavController navController;

    public AddFarmacie() {
        // Required empty public constructor
    }

    public static AddFarmacie newInstance() {
        AddFarmacie fragment = new AddFarmacie();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_farmacie, container, false);

        CRUDFarmacieOperation = (Farmacii.onCRUDFarmacieOperation) getActivity();
        prepDa = view.findViewById(R.id.prepRadioButtonDa);
        prepNu = view.findViewById(R.id.prepRadioButtonNu);
        natDa = view.findViewById(R.id.natRadioButtonDa);
        natNu = view.findViewById(R.id.natRadioButtonNu);
        adaugaButton = view.findViewById(R.id.addFarmacieButton);
        nume = view.findViewById(R.id.numeEditText);
        adresa = view.findViewById(R.id.adresaEditText);

        if (getActivity() != null) {
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        prepDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prepDa.isChecked()) {
                    preparate = true;
                }
            }
        });

        prepNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prepNu.isChecked()) {
                    preparate = false;
                }
            }
        });

        natNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (natNu.isChecked()) {
                    naturiste = false;
                }
            }
        });

        natDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (natDa.isChecked()) {
                    naturiste = true;
                }
            }
        });

        nume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numeToInsert = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adresaToInsert = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adaugaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Farmacie farmacie;

                try {
                    farmacie = new Farmacie(numeToInsert, adresaToInsert, preparate, naturiste);
                    CRUDFarmacieOperation.insertFarmacie(farmacie);
                    navController.navigate(R.id.action_addFarmacie_to_farmaciiFrag);
                    Toast.makeText(getContext(), "Farmacie adaugata cu succes ", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Eroare:" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("adaugare farmacie", e.getMessage());
                }
            }
        });

        return view;
    }
}