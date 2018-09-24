package pe.com.smartvet.fragments;

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
import pe.com.smartvet.adapters.OwnersAdapter;
import pe.com.smartvet.models.Owner;
import pe.com.smartvet.network.SmartVetService;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView ownersRecyclerView;
    private OwnersAdapter ownersAdapter;
    private RecyclerView.LayoutManager ownersLayoutManager;
    private FloatingActionButton addOwnerFloatingActionButton;
    private List<Owner> ownerList;


    public OwnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner, container, false);
        setHasOptionsMenu(true);
        ownersRecyclerView = view.findViewById(R.id.ownersRecyclerView);
        ownerList = new ArrayList<>();
        ownersAdapter = (new OwnersAdapter()).setOwners(ownerList);
        ownersLayoutManager = new LinearLayoutManager(view.getContext());
        ownersRecyclerView.setAdapter(ownersAdapter);
        ownersRecyclerView.setLayoutManager(ownersLayoutManager);
        /*addOwnerFloatingActionButton = view.findViewById(R.id.addOwnerFloatingActionButton);
        addOwnerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(), AddOwnerActivity.class));
            }
        });*/
        getOwners();
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

    private void getOwners() {
        AndroidNetworking
                .get(SmartVetService.OWNER_URL)
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ownerList = Owner.build(response.getJSONArray("owners"));
                            ownersAdapter.setOwners(ownerList);
                            ownersAdapter.notifyDataSetChanged();
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

        String ownerInput = s.toLowerCase();
        List<Owner> newList = new ArrayList<>();

        for (Owner owner : ownerList) {
            if (owner.getName().toLowerCase().contains(ownerInput)) {
                newList.add(owner);
            }
        }

        ownersAdapter.updateList(newList);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOwners();
    }
}