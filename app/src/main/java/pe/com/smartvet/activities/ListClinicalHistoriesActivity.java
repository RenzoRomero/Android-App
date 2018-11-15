package pe.com.smartvet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import pe.com.smartvet.R;
import pe.com.smartvet.adapters.ClinicalHistoriesAdapter;
import pe.com.smartvet.models.ClinicHistory;

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
        //updateClincalHistories();
    }

}
