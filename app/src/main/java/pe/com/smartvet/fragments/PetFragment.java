package pe.com.smartvet.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.activities.AddOwnerActivity;
import pe.com.smartvet.adapters.OwnersAdapter;
import pe.com.smartvet.adapters.PetsAdapter;
import pe.com.smartvet.models.Owner;
import pe.com.smartvet.models.Pet;
import pe.com.smartvet.network.SmartVetService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView petsRecyclerView;
    private PetsAdapter petsAdapter;
    private RecyclerView.LayoutManager petsLayoutManager;
    private List<Pet> petList;

    public PetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        setHasOptionsMenu(true);
        petsRecyclerView = view.findViewById(R.id.petsRecyclerView);
        petList = new ArrayList<>();
        petsAdapter = (new PetsAdapter()).setPets(petList);
        petsLayoutManager = new LinearLayoutManager(view.getContext());
        petsRecyclerView.setAdapter(petsAdapter);
        petsRecyclerView.setLayoutManager(petsLayoutManager);
        getPets();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    private void getPets() {
        AndroidNetworking
                .get(SmartVetService.PET_URL)
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            petList = Pet.build(response.getJSONArray("pets"));
                            petsAdapter.setPets(petList);
                            petsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        String petInput = s.toLowerCase();
        List<Pet> newList = new ArrayList<>();

        for (Pet pet : petList) {
            if (pet.getName().toLowerCase().contains(petInput)) {
                newList.add(pet);
            }
        }

        petsAdapter.updateList(newList);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPets();
    }
}
