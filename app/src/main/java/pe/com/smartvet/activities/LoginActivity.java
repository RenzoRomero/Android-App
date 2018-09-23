package pe.com.smartvet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.models.Vet;
import pe.com.smartvet.network.SmartVetService;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextView signUpTextView;
    ProgressBar signInProgressBar;

    boolean correctEmail = false;
    boolean correctPassword= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        signInProgressBar = findViewById(R.id.signInProgressBar);
        signUpTextView = findViewById(R.id.signUpTextView);
        signInProgressBar.setVisibility(View.GONE);
    }

    public void signInClick(View v) {
        signInProgressBar.setVisibility(View.VISIBLE);

        if(!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString()).matches()){
            emailTextInputLayout.setError(getResources().getString(R.string.invalid_email));
            correctEmail = false;
        } else {
            emailTextInputLayout.setError(null);
            correctEmail = true;
        }

        if(Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().length() == 0) {
            passwordTextInputLayout.setError(getResources().getString(R.string.invalid_password));
            correctPassword = false;
        } else {
            passwordTextInputLayout.setError(null);
            correctPassword = true;
        }

        if(correctEmail && correctPassword) {
            signIn();
        } else {
            signInProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void signIn() {
        AndroidNetworking.post(SmartVetService.SIGNIN_VET_URL)
                .addBodyParameter("email", Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("password", Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            SmartVetApp.getInstance().setToken("Bearer " + response.getString("token"));
                            SmartVetApp.getInstance().setVet(Vet.build(response.getJSONObject("vet")));
                            Toast.makeText(getApplicationContext(),  getString(R.string.hello) + " " + SmartVetApp.getInstance().getVet().getName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                            finish();
                            signInProgressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
                        signInProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void goToRegisterActivity(View v) {
        v.getContext()
                .startActivity(new Intent(v.getContext(),
                        RegisterActivity.class));
    }

}
