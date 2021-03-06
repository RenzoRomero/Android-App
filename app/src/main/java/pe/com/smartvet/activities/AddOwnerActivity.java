package pe.com.smartvet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pe.com.smartvet.R;
import pe.com.smartvet.network.SmartVetService;

public class AddOwnerActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextInputLayout nameTextInputLayout;
    TextInputLayout lastNameTextInputLayout;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextInputLayout addressTextInputLayout;
    TextInputLayout mobilePhoneTextInputLayout;
    Spinner genderSpinner;

    boolean correctEmail = false;
    boolean correctName = false;
    boolean correctLastName = false;
    boolean correctAddress = false;
    boolean correctMobilePhone = false;
    boolean correctGender = false;

    List<String> gender = new ArrayList<>();

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url = Uri.parse("");

    String email = "sos.mascotas.smartvet@gmail.com";
    String password = "smartvet123";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        addressTextInputLayout = findViewById(R.id.addressTextInputLayout);
        mobilePhoneTextInputLayout = findViewById(R.id.mobilePhoneTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);

        gender.add(getResources().getString(R.string.select_spinner));
        gender.add(getResources().getString(R.string.man_gender));
        gender.add(getResources().getString(R.string.women_gender));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void galeryClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            Uri uriSavedImage = data.getData();
            final StorageReference filepath = storageReference.child("owner").child(Objects.requireNonNull(uriSavedImage.getLastPathSegment()));
            UploadTask uploadTask = filepath.putFile(uriSavedImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        url = task.getResult();
                        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
                        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
                        photoANImageView.setImageUrl(url.toString());
                    }
                }
            });
        }
    }

    public void createOvwnerClick(View v) {
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

        if(correctEmail && correctName && correctLastName && correctMobilePhone
                && correctAddress && correctGender) {
            createOwner();
        }
    }

    private void createOwner() {
        String genderOwner = "";
        if (genderSpinner.getSelectedItem().toString().equals("Man") || genderSpinner.getSelectedItem().toString().equals("Hombre")) {
            genderOwner = "Man";
        } else {
            if (genderSpinner.getSelectedItem().toString().equals("Woman") || genderSpinner.getSelectedItem().toString().equals("Mujer")) {
                genderOwner = "Woman";
            }
        }
        AndroidNetworking.post(SmartVetService.SIGNIN_OWNER_URL)
                .addBodyParameter("email", Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("name", Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("lastName", Objects.requireNonNull(lastNameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("password", "smartvetowner")
                .addBodyParameter("address", Objects.requireNonNull(addressTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("mobilePhone", Objects.requireNonNull(mobilePhoneTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("gender", genderOwner)
                .addBodyParameter("photo", url.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("200")){
                                Toast.makeText(getApplicationContext(), R.string.vet_saved, Toast.LENGTH_SHORT).show();
                                sendMail();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_vet_saved, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_vet_saved, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMail() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        if(session != null) {
            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(email));
                message.setSubject("Smart-Vet: Bienvenido");
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString()));
                message.setContent(Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString() + " "
                        + Objects.requireNonNull(lastNameTextInputLayout.getEditText()).getText().toString() +
                        ", ha sido registrado en el sistema de Smart-Vet de la clínica SOS Mascotas", "text/html; charset=utf-8");
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
