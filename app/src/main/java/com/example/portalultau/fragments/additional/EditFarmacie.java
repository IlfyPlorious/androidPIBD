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

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.mongodb.sync.SyncConfiguration;

/*
    @author Sandu Dragos 433A
 */
public class EditFarmacie extends Fragment {

    private RadioButton prepDa, prepNu, natDa, natNu;
    private EditText nume, adresa;
    private Button adaugaButton;
    private Farmacii.onCRUDFarmacieOperation CRUDFarmacieOperation;
    private String numeToInsert, adresaToInsert;
    private boolean preparate, naturiste;
    private NavController navController;
    private String numeArg, adresaArg, idArg;
    private Boolean preparateArg, naturisteArg;

    public EditFarmacie() {
        // Required empty public constructor
    }

    public static EditFarmacie newInstance() {
        EditFarmacie fragment = new EditFarmacie();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idArg = getArguments().getString("id");
            numeArg = getArguments().getString("nume");
            adresaArg = getArguments().getString("adresa");
            preparateArg = getArguments().getBoolean("preparate");
            naturisteArg = getArguments().getBoolean("naturiste");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_farmacie, container, false);

        CRUDFarmacieOperation = (Farmacii.onCRUDFarmacieOperation) getActivity();
        prepDa = view.findViewById(R.id.editPrepRadioButtonDa);
        prepNu = view.findViewById(R.id.editPrepRadioButtonNu);
        natDa = view.findViewById(R.id.editNatRadioButtonDa);
        natNu = view.findViewById(R.id.editNatRadioButtonNu);
        adaugaButton = view.findViewById(R.id.editFarmacieButton);
        nume = view.findViewById(R.id.editNumeEditText);
        adresa = view.findViewById(R.id.editAdresaEditText);

        initializeHints();

        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        prepDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prepDa.isChecked()){
                    preparate = true;
                }
            }
        });

        prepNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( prepNu.isChecked() ){
                    preparate = false;
                }
            }
        });

        natNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( natNu.isChecked() ){
                    naturiste = false;
                }
            }
        });

        natDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( natDa.isChecked() ){
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
                    if ( idArg != null ) {
                        ObjectId newId = new ObjectId(idArg);
                        farmacie.setId(newId);
                    }
                    CRUDFarmacieOperation.updateFarmacie(farmacie);
                    navController.navigate(R.id.action_editFarmacie_to_farmaciiFrag);
                    Toast.makeText(getContext(),"Farmacie editata cu succes ", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(getContext(), "Eroare:" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("adaugare", e.toString());
                }
            }
        });

        return view;
    }

    private void initializeHints() {
        if ( numeArg != null ) {
            nume.setText(numeArg);
            numeToInsert = numeArg;
        }
        if ( adresaArg != null ) {
            adresa.setText(adresaArg);
            adresaToInsert = adresaArg;
        }
        if ( preparateArg != null ) {
            prepDa.setChecked(preparateArg);
            prepNu.setChecked(!preparateArg);
            preparate = preparateArg;
        }
        if ( naturisteArg != null ){
            natDa.setChecked(naturisteArg);
            natNu.setChecked(!naturisteArg);
            naturiste = naturisteArg;
        }
    }


}