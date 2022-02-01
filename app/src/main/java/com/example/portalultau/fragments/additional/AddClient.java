package com.example.portalultau.fragments.additional;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.fragments.Clienti;

/*
    @author Sandu Dragos 433A
 */

public class AddClient extends Fragment {

    private EditText nume, prenume, adresa, contact, varsta;
    private RadioButton premiumActiv, premiumInactiv;
    private Button adaugaButton;
    private Clienti.crudClientiOperations crudClientiOperations;
    private String numeToInsert, prenumeToInsert, contactToInsert, adresaToInsert;
    private Boolean premiumToInsert;
    private int varstaToInsert;
    private NavController navController;

    public AddClient() {
        // Required empty public constructor
    }

    public static AddClient newInstance() {
        AddClient fragment = new AddClient();
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
        View view = inflater.inflate(R.layout.fragment_add_client, container, false);

        nume = view.findViewById(R.id.numeEditTextAddClient);
        prenume = view.findViewById(R.id.prenumeEditTextAddClient);
        adresa = view.findViewById(R.id.adresaEditTextAddClient);
        contact = view.findViewById(R.id.contactEditTextAddClient);
        varsta = view.findViewById(R.id.varstaEditTextAddClient);
        premiumActiv = view.findViewById(R.id.premiumRadioButtonActiv);
        premiumInactiv = view.findViewById(R.id.premiumRadioButtonInactiv);
        adaugaButton = view.findViewById(R.id.addClientButton);
        crudClientiOperations = (Clienti.crudClientiOperations) getActivity();

        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        premiumActiv.setOnClickListener(view1 -> {
            if (premiumActiv.isChecked()){
                premiumToInsert = true;
            }
        });

        premiumInactiv.setOnClickListener(view2 -> {
            if (premiumInactiv.isChecked()){
                premiumToInsert = false;
            }
        });

        adaugaButton.setOnClickListener(view3 -> {
            Client client;
            String varstaString = varsta.getText().toString();
            if(varstaString != null && !varstaString.equals(""))
                varstaToInsert = Integer.parseInt(varstaString);

            numeToInsert = nume.getText().toString();
            prenumeToInsert = prenume.getText().toString();
            adresaToInsert = adresa.getText().toString();
            contactToInsert = contact.getText().toString();

            try{
                client = new Client(numeToInsert, prenumeToInsert, adresaToInsert, contactToInsert, varstaToInsert, premiumToInsert);
                crudClientiOperations.insertClient(client);
                navController.navigate(R.id.action_addClientFrag_to_clientiFrag);
                Toast.makeText(getContext(), "Client adaugat cu succes", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getContext(), "Eroare: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("adaugare client", e.getMessage());
            }

        });

        return view;
    }
}