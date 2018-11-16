package pe.com.smartvet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
import pe.com.smartvet.adapters.ClinicalHistoriesAdapter;
import pe.com.smartvet.models.ClinicHistory;
import pe.com.smartvet.network.SmartVetService;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class ListClinicalHistoriesActivity extends AppCompatActivity {
    List<ClinicHistory> clinicalHistories;
    RecyclerView clinicalHistoriesRecyclerView;
    ClinicalHistoriesAdapter clinicalHistoriesAdapter;
    RecyclerView.LayoutManager clinicalHistoriesLayoutManager;
    private int spanCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clinical_histories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clinicalHistoriesRecyclerView = (RecyclerView) findViewById(R.id.clinicalHistoriesRecyclerView);
        clinicalHistories = new ArrayList<>();
        clinicalHistoriesAdapter = new ClinicalHistoriesAdapter(clinicalHistories);

        spanCount = getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT ? 2 : 3;
        clinicalHistoriesLayoutManager = new GridLayoutManager(this , spanCount);
        clinicalHistoriesRecyclerView.setAdapter(clinicalHistoriesAdapter);
        clinicalHistoriesRecyclerView.setLayoutManager(clinicalHistoriesLayoutManager);
        getClinicalHistories();
    }

    public void getClinicalHistories() {
        AndroidNetworking
                .get(SmartVetService.CLINIC_HISTORY_PET_URL)
                .addPathParameter("pet_id", SmartVetApp.getInstance().getPet().getId())
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            clinicalHistories = ClinicHistory.build(response.getJSONArray("clinicalHistories"));
                            clinicalHistoriesAdapter.setClinicalHistories(clinicalHistories);
                            clinicalHistoriesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

}
