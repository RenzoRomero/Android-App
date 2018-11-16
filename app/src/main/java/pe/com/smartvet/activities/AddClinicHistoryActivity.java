package pe.com.smartvet.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.network.SmartVetService;

public class AddClinicHistoryActivity extends AppCompatActivity {
    EditText dateEditText;
    TextInputLayout weightTextInputLayout;
    TextInputLayout heightTextInputLayout;
    TextInputLayout detailTextInputLayout;

    Boolean correctWeight= false;
    Boolean correctHeight= false;
    Boolean correctDate = false;
    Boolean correctDetail = false;

    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dateEditText = findViewById(R.id.dateEditText);
        weightTextInputLayout = findViewById(R.id.weightTextInputLayout);
        heightTextInputLayout = findViewById(R.id.heightTextInputLayout);
        detailTextInputLayout = findViewById(R.id.detailTextInputLayout);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButton();
            }
        });

        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                correctDate = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onDateButton() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void createClinicHistoryClick(View view) {
        if(Objects.requireNonNull(weightTextInputLayout.getEditText()).getText().toString().length() == 0) {
            weightTextInputLayout.setError(getResources().getString(R.string.empty_weight));
            correctWeight = false;
        } else {
            weightTextInputLayout.setError(null);
            correctWeight = true;
        }

        if(Objects.requireNonNull(heightTextInputLayout.getEditText()).getText().toString().length() == 0) {
            heightTextInputLayout.setError(getResources().getString(R.string.empty_height));
            correctHeight = false;
        } else {
            heightTextInputLayout.setError(null);
            correctHeight = true;
        }

        if(Objects.requireNonNull(detailTextInputLayout.getEditText()).getText().toString().length() == 0) {
            detailTextInputLayout.setError(getResources().getString(R.string.empty_detail));
            correctDetail = false;
        } else {
            detailTextInputLayout.setError(null);
            correctDetail = true;
        }

        if(correctHeight && correctWeight && correctDate && correctDetail) {
            saveClinicHistory();
        }
    }

    private void saveClinicHistory() {
        AndroidNetworking.post(SmartVetService.CLINIC_HISTORY_URL)
                .addBodyParameter("pet", SmartVetApp.getInstance().getPet().getId())
                .addBodyParameter("date", dateEditText.getText().toString())
                .addBodyParameter("weight", Objects.requireNonNull(weightTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("height", Objects.requireNonNull(heightTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("details", Objects.requireNonNull(detailTextInputLayout.getEditText()).getText().toString())
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.clinic_history_save, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_clinic_history_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
