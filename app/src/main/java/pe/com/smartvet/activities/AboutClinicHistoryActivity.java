package pe.com.smartvet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.models.ClinicHistory;

public class AboutClinicHistoryActivity extends AppCompatActivity {
    ANImageView photoANImageView;
    TextView nameTextView;
    TextView dateTextView;
    TextView weightTextView;
    TextView heightTextView;
    TextView detailsTextView;

    ClinicHistory clinicHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_clinic_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        dateTextView = findViewById(R.id.dateTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        detailsTextView = findViewById(R.id.detailsTextView);

        clinicHistory = SmartVetApp.getInstance().getClinicHistory();

        setClinicHistoryInformation();
    }

    private void setClinicHistoryInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(clinicHistory.getPet().getPhoto());
        nameTextView.setText(clinicHistory.getPet().getName());
        dateTextView.setText(clinicHistory.getDate());
        weightTextView.setText(clinicHistory.getWeight() + " Kg");
        heightTextView.setText(clinicHistory.getHeight() + " cm");
        detailsTextView.setText(clinicHistory.getDetails());
    }
}
