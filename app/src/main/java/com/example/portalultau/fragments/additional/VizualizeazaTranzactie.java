package com.example.portalultau.fragments.additional;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portalultau.R;

/*
    @author: Sandu Dragos 433A
 */

public class VizualizeazaTranzactie extends Fragment {

    private Button editeaza, inchide;
    private TextView id, client, contactClient, farmacie, adresaFarmacie, produsXcantitate, suma, tipPlata,data;
    private ImageView imgTipPlata;

    private String argId, argClient, argContact, argFarmacie, argAdresa, argProdXCan, argTipPlata, argSuma, argData;

    private NavController navController;

    public VizualizeazaTranzactie() {

    }

    public static VizualizeazaTranzactie newInstance(String param1, String param2) {
        VizualizeazaTranzactie fragment = new VizualizeazaTranzactie();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            argId = getArguments().getString("id");
            argClient = "Client: " + getArguments().getString("numeClient");
            argContact = "Contact client: " + getArguments().getString("contactClient");
            argFarmacie = "Farmacie: " + getArguments().getString("farmacie");
            argAdresa = "Adresa farmacie: " + getArguments().getString("adresaFarmacie");
            argProdXCan = "Produs: " +
                    getArguments().getString("produs") + " x " +
                    getArguments().getString("cantitate") + " buc";
            argSuma = "Suma totala: " + String.valueOf(getArguments().getFloat("suma")) + " lei";
            if (getArguments().getString("tipPlata").equals("card")){
                argTipPlata = "Tip de plata: card";
            } else {
                argTipPlata = "Tip de plata: numerar";
            }
            argData = "Data: " + getArguments().getString("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vizualizeaza_tranzactie, container, false);

        id = view.findViewById(R.id.idTranzactieDetalii);
        client = view.findViewById(R.id.clientDetalii);
        contactClient = view.findViewById(R.id.contactClientDetalii);
        farmacie = view.findViewById(R.id.farmacieDetalii);
        adresaFarmacie = view.findViewById(R.id.adresaFarmacieDetalii);
        produsXcantitate = view.findViewById(R.id.produsXcantitateDetalii);
        suma = view.findViewById(R.id.sumaDetalii);
        tipPlata = view.findViewById(R.id.tipPlataDetalii);
        imgTipPlata = view.findViewById(R.id.imgTipPlataDetalii);
        data = view.findViewById(R.id.dataDetalii);
        editeaza = view.findViewById(R.id.editeazaButtonDetalii);
        inchide = view.findViewById(R.id.inchideButtonDetalii);

        initViews();

        if(getActivity() != null){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        inchide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_vizualizeazaTranzactie_to_tranzactiiFrag);
            }
        });

        editeaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("id", argId);
                navController.navigate(R.id.action_vizualizeazaTranzactie_to_editeazaTranzactie, args);
            }
        });

        return view;
    }

    private void initViews() {
        id.setText(argId);
        client.setText(argClient);
        contactClient.setText(argContact);
        farmacie.setText(argFarmacie);
        adresaFarmacie.setText(argAdresa);
        produsXcantitate.setText(argProdXCan);
        suma.setText(argSuma);
        tipPlata.setText(argTipPlata);
        data.setText(argData);
        if (argTipPlata.contains("card")) imgTipPlata.setImageResource(R.drawable.card);
            else imgTipPlata.setImageResource(R.drawable.cash);
    }
}