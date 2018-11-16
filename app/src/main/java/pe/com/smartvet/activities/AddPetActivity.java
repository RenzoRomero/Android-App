package pe.com.smartvet.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.network.SmartVetService;

public class AddPetActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    Button galeryButton;
    TextInputLayout nameTextInputLayout;
    EditText dateEditText;
    TextInputLayout breedTextInputLayout;
    Spinner genderSpinner;

    Boolean correctName = false;
    Boolean correctBreed= false;
    Boolean correctDate = false;
    Boolean correctGender = false;
    Boolean correctUrl = false;

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url;

    int day;
    int month;
    int year;

    List<String> gender = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        galeryButton = findViewById(R.id.galeryButton);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        dateEditText = findViewById(R.id.dateEditText);
        breedTextInputLayout = findViewById(R.id.breedTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);

        gender.add(getResources().getString(R.string.select_spinner));
        gender.add(getResources().getString(R.string.male_gender));
        gender.add(getResources().getString(R.string.female_gender));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);

        storageReference = FirebaseStorage.getInstance().getReference();

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
            final StorageReference filepath = storageReference.child("product").child(Objects.requireNonNull(uriSavedImage.getLastPathSegment()));
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

    public void createPetClick(View view) {
        if(Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString().length() == 0) {
            nameTextInputLayout.setError(getResources().getString(R.string.empty_name));
            correctName = false;
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if(Objects.requireNonNull(breedTextInputLayout.getEditText()).getText().toString().length() == 0) {
            breedTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctBreed = false;
        } else {
            breedTextInputLayout.setError(null);
            correctBreed = true;
        }

        if(genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), R.string.invalid_gender, Toast.LENGTH_SHORT).show();
        } else {
            correctGender = true;
        }

        if( url == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_photo, Toast.LENGTH_SHORT ).show();
            correctUrl = false;
        } else {
            correctUrl = true;
        }

        if(correctName && correctBreed && correctDate && correctGender && correctUrl) {
            savePet();
        }
    }

    private void savePet() {
        String genderPet = "";
        if (genderSpinner.getSelectedItem().toString().equals("Male") || genderSpinner.getSelectedItem().toString().equals("Macho")) {
            genderPet = "Male";
        } else {
            if (genderSpinner.getSelectedItem().toString().equals("Female") || genderSpinner.getSelectedItem().toString().equals("Hembra")) {
                genderPet = "Female";
            }
        }
        AndroidNetworking.post(SmartVetService.PET_URL)
                .addBodyParameter("name", Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("breed", Objects.requireNonNull(breedTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("owner", SmartVetApp.getInstance().getOwner().getId())
                .addBodyParameter("birthdate", dateEditText.getText().toString())
                .addBodyParameter("gender", genderPet)
                .addBodyParameter("photo", url.toString())
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.pet_save, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_pet_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
