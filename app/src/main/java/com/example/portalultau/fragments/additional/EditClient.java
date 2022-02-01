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

import org.bson.types.ObjectId;

/*
    @author Sandu Dragos 433A
 */
public class EditClient extends Fragment {

    private EditText nume, prenume, adresa, contact, varsta;
    private RadioButton premiumActiv, premiumInactiv;
    private Button salveazaButton;
    private Clienti.crudClientiOperations crudClientiOperations;
    private String numeToInsert, prenumeToInsert, contactToInsert, adresaToInsert;
    private Boolean premiumToInsert;
    private int varstaToInsert;
    private NavController navController;
    private String numeArg, prenumeArg, adresaArg, idArg, contactArg;
    private Boolean premiumArg;
    private int varstaArg;

    public EditClient() {
        // Required empty public constructor
    }

    public static EditClient newInstance() {
        EditClient fragment = new EditClient();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null ){
            idArg = getArguments().getString("id");
            numeArg = getArguments().getString("nume");
            prenumeArg = getArguments().getString("prenume");
            adresaArg = getArguments().getString("adresa");
            contactArg = getArguments().getString("contact");
            premiumArg = getArguments().getBoolean("premium");
            varstaArg = getArguments().getInt("varsta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_client, container, false);

        nume = view.findViewById(R.id.numeEditTextEditClient);
        prenume = view.findViewById(R.id.prenumeEditTextEditClient);
        adresa = view.findViewById(R.id.adresaEditTextEditClient);
        contact = view.findViewById(R.id.contactEditTextEditClient);
        varsta = view.findViewById(R.id.varstaEditTextEditClient);
        premiumActiv = view.findViewById(R.id.premiumRadioButtonActivEdit);
        premiumInactiv = view.findViewById(R.id.premiumRadioButtonInactivEdit);
        salveazaButton = view.findViewById(R.id.editClientButton);
        crudClientiOperations = (Clienti.crudClientiOperations) getActivity();

        initializeHints();

        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        premiumActiv.setOnClickListener(view1 -> {
            if (premiumActiv.isChecked()){
                premiumToInsert = true;
            }
        });

        premiumInactiv.setOnClickListener(view2 ->{
            if (premiumInactiv.isChecked()){
                premiumToInsert = false;
            }
        });

        salveazaButton.setOnClickListener(view3 -> {
            Client client;

            String varstaString = varsta.getText().toString();
            if(!varstaString.equals(""))
                varstaToInsert = Integer.parseInt(varstaString);

            numeToInsert = nume.getText().toString();
            prenumeToInsert = prenume.getText().toString();
            adresaToInsert = adresa.getText().toString();
            contactToInsert = contact.getText().toString();

            try{
                client = new Client(numeToInsert, prenumeToInsert, adresaToInsert, contactToInsert, varstaToInsert, premiumToInsert);
                if(idArg != null){
                    ObjectId newId = new ObjectId(idArg);
                    client.set_id(newId);
                }
                crudClientiOperations.updateClient(client);
                navController.navigate(R.id.action_editClient_to_clientiFrag);
                Toast.makeText(getContext(), "Client editat cu succes", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getContext(), "Eroare: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("adaugare client", e.getMessage());
            }

        });

        return view;
    }

    private void initializeHints() {
        if(numeArg != null){
            nume.setText(numeArg);
            numeToInsert = numeArg;
        }
        if(prenumeArg != null){
            prenume.setText(prenumeArg);
            prenumeToInsert = prenumeArg;
        }
        if(adresaArg != null){
            adresa.setText(adresaArg);
            adresaToInsert = adresaArg;
        }
        if(contactArg != null){
            contact.setText(contactArg);
            contactToInsert = contactArg;
        }
        if(premiumArg != null){
            premiumActiv.setChecked(premiumArg);
            premiumInactiv.setChecked(!premiumArg);
            premiumToInsert = premiumArg;
        }
        if(varstaArg != 0){
            varsta.setText(String.valueOf(varstaArg));
            varstaToInsert = varstaArg;
        }
    }
}