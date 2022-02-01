package com.example.portalultau.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portalultau.R;
import com.example.portalultau.database.Client;
import com.example.portalultau.database.Farmacie;
import com.example.portalultau.recyclerviews.ClientiRecyclerView;
import com.example.portalultau.recyclerviews.FarmaciiRecyclerView;

import java.util.ArrayList;

/*
    @author Sandu Dragos
 */
public class Clienti extends Fragment {

    private ImageButton addClient,reloadBtn;
    private NavController navController;
    private SearchView searchView;
    private TextView heroLabel;
    private RecyclerView recyclerView;
    private ArrayList<Client> clientiList = new ArrayList<>();
    private clientiRealmComm comm;

    public interface clientiRealmComm{
        ArrayList<Client> citesteClienti();
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clienti, container, false);

        comm = (clientiRealmComm) getActivity();
        searchView = view.findViewById(R.id.searchClienti);
        heroLabel = view.findViewById(R.id.clientiHeroText);
        addClient = view.findViewById(R.id.addClient);
        reloadBtn = view.findViewById(R.id.reloadClientiBtn);

        if ( getActivity() != null ){
            navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        }

        clientiList = comm.citesteClienti();
        recyclerView = view.findViewById(R.id.clientiRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ClientiRecyclerView adapter = new ClientiRecyclerView(clientiList, getActivity());
        recyclerView.setAdapter(adapter);

        addClient.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_clientiFrag_to_addClientFrag);
        });

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnSearchClickListener(view12 -> heroLabel.setVisibility(View.INVISIBLE));

        searchView.setOnCloseListener(() -> {
            heroLabel.setVisibility(View.VISIBLE);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        reloadBtn.setOnClickListener(view13 -> {
            clientiList = comm.citesteClienti();
            adapter.updateDataSet(clientiList);
            Toast.makeText(getContext(), "Lista de clienti a fost actualizata", Toast.LENGTH_SHORT).show();
            if (adapter.getItemCount() - 1 > 0)
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

        });

        return view;
    }

    public interface crudClientiOperations{
        void insertClient(Client client);
        void updateClient(Client client);
        void deleteClient(Client client);
    }
}