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
import pe.com.smartvet.adapters.PromotionsAdapter;
import pe.com.smartvet.models.Promotion;
import pe.com.smartvet.network.SmartVetService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView promotionsRecyclerView;
    private PromotionsAdapter promotionsAdapter;
    private RecyclerView.LayoutManager promotionsLayoutManager;
    private FloatingActionButton addPromotionFloatingActionButton;
    private List<Promotion> promotionList;


    public PromotionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);
        setHasOptionsMenu(true);
        promotionsRecyclerView = view.findViewById(R.id.promotionsRecyclerView);
        promotionList = new ArrayList<>();
        promotionsAdapter = (new PromotionsAdapter()).setPromotions(promotionList);
        promotionsLayoutManager = new LinearLayoutManager(view.getContext());
        promotionsRecyclerView.setAdapter(promotionsAdapter);
        promotionsRecyclerView.setLayoutManager(promotionsLayoutManager);

        getPromotions();
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

    private void getPromotions() {
        AndroidNetworking
                .get(SmartVetService.PROMOTION_URL)
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            promotionList = Promotion.build(response.getJSONArray("promotions"));
                            promotionsAdapter.setPromotions(promotionList);
                            promotionsAdapter.notifyDataSetChanged();
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

        String promotionInput = s.toLowerCase();
        List<Promotion> newList = new ArrayList<>();

        for(Promotion promotion : promotionList) {
            if(promotion.getName().toLowerCase().contains(promotionInput)) {
                newList.add(promotion);
            }
        }

        promotionsAdapter.updateList(newList);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPromotions();
    }

}
