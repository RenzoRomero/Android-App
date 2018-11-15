package pe.com.smartvet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.adapters.PetsAdapter;
import pe.com.smartvet.models.Pet;
import pe.com.smartvet.network.SmartVetService;

public class ListPetsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView petsRecyclerView;
    PetsAdapter petsAdapter;
    RecyclerView.LayoutManager petsLayoutManager;
    List<Pet> petList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        petsRecyclerView = findViewById(R.id.petsRecyclerView);
        petList = new ArrayList<>();
        petsAdapter = (new PetsAdapter()).setPets(petList);
        petsLayoutManager = new LinearLayoutManager(this);
        petsRecyclerView.setAdapter(petsAdapter);
        petsRecyclerView.setLayoutManager(petsLayoutManager);
        getPetsByOwner();
    }

    public void getPetsByOwner() {
        AndroidNetworking
                .get(SmartVetService.PET_OWNER_URL)
                .addPathParameter("owner_id", SmartVetApp.getInstance().getOwner().getId())
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
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

}
