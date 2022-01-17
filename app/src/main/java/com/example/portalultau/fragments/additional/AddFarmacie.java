package com.example.portalultau.fragments.additional;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.portalultau.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFarmacie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFarmacie extends Fragment {

    private RadioButton prepDa, prepNu, natDa, natNu;

    public AddFarmacie() {
        // Required empty public constructor
    }

    public static AddFarmacie newInstance(String param1, String param2) {
        AddFarmacie fragment = new AddFarmacie();
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
        View view = inflater.inflate(R.layout.fragment_add_farmacie, container, false);

        prepDa = view.findViewById(R.id.prepRadioButtonDa);
        prepNu = view.findViewById(R.id.prepRadioButtonNu);
        natDa = view.findViewById(R.id.natRadioButtonDa);
        natNu = view.findViewById(R.id.natRadioButtonNu);

        prepDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prepDa.isChecked()){

                }
            }
        });

        return view;
    }
}