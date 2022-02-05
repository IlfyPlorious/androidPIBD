package com.example.portalultau.fragments.additional;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.database.Tranzactie;
import com.example.portalultau.fragments.Clienti;
import com.example.portalultau.fragments.Farmacii;
import com.example.portalultau.fragments.Tranzactii;
import com.example.portalultau.helpers.Produs;

import org.bson.types.ObjectId;

import java.util.ArrayList;

/*
    @author: Sandu Dragos 433A
 */

public class AddTranzactie extends Fragment {

    private Spinner clienti, farmacii, produse;
    private Button adaugaButton;
    private EditText cantitate,data;
    private RadioButton card, numerar;
    private TextView suma;
    private DatePickerDialog datePickerDialog;
    private ArrayList<Produs> listaProduse = new ArrayList<>();
    private ArrayList<Client> listaClienti = new ArrayList<>();
    private ArrayList<Farmacie> listaFarmacii = new ArrayList<>();

    private ObjectId idClient, idFarmacie;
    private String produs, dataDeAdaugat, cantitateDeAdaugat, tipDePlata;
    private float sumaDeAdaugat = 0;

    private Clienti.clientiRealmComm realmComm;
    private Farmacii.farmaciiRealmComm realmCommFarma;
    private Tranzactii.crudTranzactiiOperations crudTranzactiiOperations;

    private NavController navController;

    public AddTranzactie() {
        // Required empty public constructor
    }

    public static AddTranzactie newInstance() {
        AddTranzactie fragment = new AddTranzactie();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_tranzactie, container, false);

        clienti = view.findViewById(R.id.clientSpinner);
        farmacii = view.findViewById(R.id.farmacieSpinner);
        produse = view.findViewById(R.id.produsSpinner);
        cantitate = view.findViewById(R.id.cantitateAddTranzactie);
        suma = view.findViewById(R.id.sumaAddTranzactie);
        card = view.findViewById(R.id.tipDePlataCard);
        numerar = view.findViewById(R.id.tipDePlataNumerar);
        data = view.findViewById(R.id.dataAddTranzactie);
        adaugaButton = view.findViewById(R.id.addTranzactieBtn);
        realmComm = (Clienti.clientiRealmComm) getActivity();
        realmCommFarma = (Farmacii.farmaciiRealmComm) getActivity();
        crudTranzactiiOperations = (Tranzactii.crudTranzactiiOperations) getActivity();

        if (getActivity() != null) {
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        data.setInputType(InputType.TYPE_NULL);
        data.setOnClickListener(view1 -> {
            final Calendar calendar = Calendar.getInstance();
            int zi = calendar.get(Calendar.DAY_OF_MONTH);
            int luna = calendar.get(Calendar.MONTH);
            int an = calendar.get(Calendar.YEAR);

            datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    data.setText( day + "/" + (month + 1) + "/" + year );
                    dataDeAdaugat = day + "/" + (month + 1) + "/" + year;
                }
            }, an, luna, zi);

            datePickerDialog.show();

        });

        cantitate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Produs produs = (Produs) produse.getSelectedItem();
                cantitateDeAdaugat = charSequence.toString();
                setSuma(produs);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.isChecked())
                    tipDePlata = "card";
            }
        });

        numerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numerar.isChecked())
                    tipDePlata = "numerar";
            }
        });

        ///spinner produse

        for(int i=1; i<10; i++){
            listaProduse.add(new Produs("Produs " + i, (float)i*7/2));
        }

        ArrayAdapter<?> adapterProduse = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listaProduse);
        adapterProduse.setDropDownViewResource(R.layout.spinner_item);
        produse.setAdapter(adapterProduse);

        produse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Produs selectedProdus = (Produs) adapterView.getItemAtPosition(pos);
                setSuma(selectedProdus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /// spinner clienti

        listaClienti = realmComm.citesteClienti();
        ArrayAdapter<?> adapterClienti = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listaClienti);
        adapterClienti.setDropDownViewResource(R.layout.spinner_item);
        clienti.setAdapter(adapterClienti);

        clienti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                Client client = (Client) adapterView.getItemAtPosition(pos);
                idClient = client.get_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner farmacii

        listaFarmacii = realmCommFarma.citesteFarmacii();
        ArrayAdapter<?> adapterFarmacii = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listaFarmacii);
        adapterClienti.setDropDownViewResource(R.layout.spinner_item);
        farmacii.setAdapter(adapterFarmacii);

        farmacii.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                Farmacie farmacie = (Farmacie) adapterView.getItemAtPosition(pos);
                idFarmacie = farmacie.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adaugaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tranzactie tranzactie;
                try{
                    tranzactie = new Tranzactie(dataDeAdaugat, sumaDeAdaugat, produse.getSelectedItem().toString(), tipDePlata, cantitateDeAdaugat, idFarmacie, idClient);
                    crudTranzactiiOperations.insertTranzactie(tranzactie);
                    navController.navigate(R.id.action_addTranzactie_to_tranzactiiFrag);
                } catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(getContext(), "Eroare la incarcarea in baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void setSuma(Produs selectedProdus) {
        if (!cantitate.getText().toString().equals("")) {
            suma.setText("Suma: " + selectedProdus.getPret() * Integer.parseInt(cantitate.getText().toString()) + " lei");
            sumaDeAdaugat = selectedProdus.getPret() * Integer.parseInt(cantitate.getText().toString());
        }
        else {
            suma.setText("Suma: 0.0 lei");
            sumaDeAdaugat = 0;
        }
    }


}