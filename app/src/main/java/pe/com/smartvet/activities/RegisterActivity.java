package pe.com.smartvet.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import pe.com.smartvet.R;
import pe.com.smartvet.network.SmartVetService;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout nameTextInputLayout;
    TextInputLayout lastNameTextInputLayout;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextInputLayout addressTextInputLayout;
    TextInputLayout mobilePhoneTextInputLayout;
    Spinner genderSpinner;
    TextView signInTextView;
    Button signUpButton;
    ProgressBar signUpProgressBar;

    boolean correctEmail = false;
    boolean correctPassword= false;
    boolean correctName = false;
    boolean correctLastName = false;
    boolean correctAddress = false;
    boolean correctMobilePhone = false;
    boolean correctGender = false;

    List<String> gender = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);
        mobilePhoneTextInputLayout = findViewById(R.id.mobilePhoneTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);

        signInTextView = findViewById(R.id.signInTextView);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
        signUpButton = findViewById(R.id.signUpButton);
        signUpProgressBar.setVisibility(View.GONE);

        gender.add(getResources().getString(R.string.select_spinner));
        gender.add(getResources().getString(R.string.man_gender));
        gender.add(getResources().getString(R.string.women_gender));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);
    }

    public void signUpClick(View v) {
        signUpProgressBar.setVisibility(View.VISIBLE);

        if(!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Záéíóú ]+$");
        if (!pattern.matcher(Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString()).matches()
                || (nameTextInputLayout.getEditText().getText().toString().length() > 30)) {
            nameTextInputLayout.setError(getResources().getString(R.string.invalid_name));
            correctName = false;
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if (!pattern.matcher(Objects.requireNonNull(lastNameTextInputLayout.getEditText()).getText().toString()).matches()
                || lastNameTextInputLayout.getEditText().getText().toString().length() > 30) {
            lastNameTextInputLayout.setError(getResources().getString(R.string.invalid_lastName));
            correctLastName = false;
        } else {
            lastNameTextInputLayout.setError(null);
            correctLastName = true;
        }

        if(Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().length() < 8) {
            passwordTextInputLayout.setError(getResources().getString(R.string.invalid_password));
            correctPassword = false;
        } else {
            passwordTextInputLayout.setError(null);
            correctPassword = true;
        }

        if(Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().toString().length() == 0) {
            addressTextInputLayout.setError(getResources().getString(R.string.invalid_address));
            correctAddress = false;
        } else {
            addressTextInputLayout.setError(null);
            correctAddress = true;
        }

        if(Objects.requireNonNull(mobilePhoneTextInputLayout.getEditText()).getText().toString().length() == 0) {
            mobilePhoneTextInputLayout.setError(getResources().getString(R.string.invalid_mobilePhone));
            correctMobilePhone = false;
        } else {
            mobilePhoneTextInputLayout.setError(null);
            correctMobilePhone = true;
        }

        if(genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.invalid_gender, Toast.LENGTH_SHORT).show();
        } else {
            correctGender = true;
        }

        if(correctEmail && correctPassword && correctName && correctLastName && correctMobilePhone
                && correctAddress && correctGender) {
            signUpUser();
        } else {
            signUpProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void signUpUser() {
        AndroidNetworking.post(SmartVetService.SIGNUP_VET_URL)
                .addBodyParameter("email", Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("name", Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("lastName", Objects.requireNonNull(lastNameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("password", Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("address", Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("mobilePhone", Objects.requireNonNull(mobilePhoneTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("gender", genderSpinner.getSelectedItem().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("200")){
                                Toast.makeText(getApplicationContext(), R.string.vet_saved, Toast.LENGTH_SHORT).show();
                                finish();
                                signUpProgressBar.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_user_saved, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_user_saved, Toast.LENGTH_SHORT).show();
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void goToLoginActivity(View v) {
        finish();
    }

}
