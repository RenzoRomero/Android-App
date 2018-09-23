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
import pe.com.smartvet.activities.AddProductActivity;
import pe.com.smartvet.adapters.ProductsAdapter;
import pe.com.smartvet.models.Product;
import pe.com.smartvet.network.SmartVetService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private RecyclerView.LayoutManager productsLayoutManager;
    private FloatingActionButton addProductFloatingActionButton;
    private List<Product> productList;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        setHasOptionsMenu(true);
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productList = new ArrayList<>();
        productsAdapter = (new ProductsAdapter()).setProducts(productList);
        productsLayoutManager = new LinearLayoutManager(view.getContext());
        productsRecyclerView.setAdapter(productsAdapter);
        productsRecyclerView.setLayoutManager(productsLayoutManager);
        addProductFloatingActionButton = view.findViewById(R.id.addProductFloatingActionButton);
        addProductFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(), AddProductActivity.class));
            }
        });
        getProducts();
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

    private void getProducts() {
        AndroidNetworking
                .get(SmartVetService.PRODUCT_URL)
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            productList = Product.build(response.getJSONArray("products"));
                            productsAdapter.setProducts(productList);
                            productsAdapter.notifyDataSetChanged();
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

        String productInput = s.toLowerCase();
        List<Product> newList = new ArrayList<>();

        for(Product product : productList) {
            if(product.getName().toLowerCase().contains(productInput)) {
                newList.add(product);
            }
        }

        productsAdapter.updateList(newList);
        return true;
    }
}
