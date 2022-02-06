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
import java.util.List;
import java.util.stream.Collectors;

/*
    @author: Sandu Dragos 433A
 */
public class EditeazaTranzactie extends Fragment {

    private Spinner clienti, farmacii, produse;
    private Button salveazaBtn;
    private EditText cantitate,data;
    private RadioButton card, numerar;
    private TextView suma;
    private DatePickerDialog datePickerDialog;
    private ArrayList<Produs> listaProduse = new ArrayList<>();
    private ArrayList<Client> listaClienti = new ArrayList<>();
    private ArrayList<Farmacie> listaFarmacii = new ArrayList<>();
    private String idTranz;
    private ArrayAdapter<?> adapterFarmacii;
    private ArrayAdapter<?> adapterClienti;
    private ArrayAdapter<?> adapterProduse;

    private ObjectId idClient, idFarmacie;
    private String produs, dataDeAdaugat, cantitateDeAdaugat, tipDePlata;
    private float sumaDeAdaugat = 0;

    private Clienti.clientiRealmComm realmComm;
    private Farmacii.farmaciiRealmComm realmCommFarma;
    private Tranzactii.crudTranzactiiOperations crudTranzactiiOperations;

    private NavController navController;

    public EditeazaTranzactie() {
        // Required empty public constructor
    }

    public static EditeazaTranzactie newInstance(String param1, String param2) {
        EditeazaTranzactie fragment = new EditeazaTranzactie();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idTranz = getArguments().getString("id");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editeaza_tranzactie, container, false);

        clienti = view.findViewById(R.id.clientSpinnerEdit);
        farmacii = view.findViewById(R.id.farmacieSpinnerEdit);
        produse = view.findViewById(R.id.produsSpinnerEdit);
        cantitate = view.findViewById(R.id.cantitateEditTranzactie);
        suma = view.findViewById(R.id.sumaEditTranzactie);
        card = view.findViewById(R.id.tipDePlataCardEdit);
        numerar = view.findViewById(R.id.tipDePlataNumerarEdit);
        data = view.findViewById(R.id.dataEditTranzactie);
        salveazaBtn = view.findViewById(R.id.editTranzactieBtn);
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

        //spinner produse

        for(int i=1; i<10; i++){
            listaProduse.add(new Produs("Produs " + i, (float)i*7/2));
        }

        adapterProduse = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listaProduse);
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

        //spinner clienti

        listaClienti = realmComm.citesteClienti();
        adapterClienti = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listaClienti);
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
        adapterFarmacii = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listaFarmacii);
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

        initFieldContents();

        salveazaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tranzactie tranzactie;
                ObjectId id = new ObjectId(idTranz);
                try{
                    tranzactie = new Tranzactie(dataDeAdaugat, sumaDeAdaugat, produse.getSelectedItem().toString(), tipDePlata, cantitateDeAdaugat, idFarmacie, idClient);
                    tranzactie.set_id(id);
                    crudTranzactiiOperations.updateTranzactie(tranzactie);
                    navController.navigate(R.id.action_editeazaTranzactie_to_tranzactiiFrag);
                } catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(getContext(), "Eroare la incarcarea in baza de date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initFieldContents() {
        Tranzactie tranzactie = crudTranzactiiOperations.getTranzactie(idTranz);
        Client client = getClientForTranzactie(tranzactie);
        Farmacie farmacie = getFarmacieForTranzactie(tranzactie);
        int position;
        for(position = 0; position < adapterClienti.getCount(); position++){
            if(adapterClienti.getItem(position) == client)
                clienti.setSelection(position);
        }
        for(position = 0; position < adapterFarmacii.getCount(); position++){
            if(adapterFarmacii.getItem(position) == farmacie)
                farmacii.setSelection(position);
        }
        for(position = 0; position < adapterProduse.getCount(); position++){
            if(adapterProduse.getItem(position).toString().equals(tranzactie.getProdus()))
                produse.setSelection(position);
        }

        cantitate.setText(tranzactie.getCantitateProdus());
        suma.setText("Suma: " + String.valueOf(tranzactie.getSuma()) + " lei");
        if (tranzactie.getTipPlata().equals("card"))
            card.setChecked(true);
        else numerar.setChecked(false);

        data.setText(tranzactie.getData());

        idFarmacie = farmacie.getId();
        idClient = client.get_id();
        cantitateDeAdaugat = tranzactie.getCantitateProdus();
        sumaDeAdaugat = tranzactie.getSuma();
        tipDePlata = tranzactie.getTipPlata();
        dataDeAdaugat = tranzactie.getData();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Client getClientForTranzactie(Tranzactie tranzactie){
        Client client;

        List<Client> getClientList;
        getClientList = listaClienti.stream().filter(client1 -> client1.get_id().equals(tranzactie.getIdClient()))
                .collect(Collectors.toList());

        if (getClientList.size() != 0)
            client = getClientList.get(0);
        else client = new Client("client sters", "client sters", "client sters", "client sters", 1, false);

        return client;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Farmacie getFarmacieForTranzactie(Tranzactie tranzactie){
        Farmacie farmacie;

        List<Farmacie> getFarmacieList;
        getFarmacieList = listaFarmacii.stream().filter(farmacie1 -> farmacie1.getId().equals(tranzactie.getIdFarmacie()))
                .collect(Collectors.toList());

        if (getFarmacieList.size() != 0)
        farmacie = getFarmacieList.get(0);
        else farmacie = new Farmacie("farmacie stearsa", "farmacie stearsa", false, false);

        return farmacie;
    }
}