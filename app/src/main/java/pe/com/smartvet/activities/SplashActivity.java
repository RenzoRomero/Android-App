package pe.com.smartvet.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class SplashActivity extends AppCompatActivity {

    Vet vet;
    String token;

    private static final String STRING_PREFERENCES = "preferences";
    private final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                if(!getData(SplashActivity.this,"user").equals("") && !getData(SplashActivity.this,"password").equals("")) {
                    signIn();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                }
            };
        }, SPLASH_DURATION);
    }

    public static void setData(Context context, String keyPref, String data) {
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(keyPref, data).apply();
    }

    public static void changeData(Context context, String keyPref, String data) {
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(keyPref, data).apply();
    }

    public String getData(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getString(keyPref, "");
    }

    private void signIn() {
        AndroidNetworking.post(SmartVetService.SIGNIN_VET_URL)
                .addBodyParameter("email", getData(SplashActivity.this,"user"))
                .addBodyParameter("password", getData(SplashActivity.this,"password"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            SmartVetApp.getInstance().setToken("Bearer " + response.getString("token"));
                            SmartVetApp.getInstance().setVet(Vet.build(response.getJSONObject("vet")));
                            SmartVetApp.getInstance().getVet().setPassword(getData(SplashActivity.this,"password"));
                            Toast.makeText(getApplicationContext(),  getString(R.string.hello) + " " + SmartVetApp.getInstance().getVet().getName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }
                });
    }

}
