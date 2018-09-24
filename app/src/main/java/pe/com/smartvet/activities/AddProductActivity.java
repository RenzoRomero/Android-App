package pe.com.smartvet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.network.SmartVetService;

public class AddProductActivity extends AppCompatActivity {

    ANImageView photoANImageView;
    TextInputLayout brandTextInputLayout;
    TextInputLayout nameTextInputLayout;
    TextInputLayout descriptionTextInputLayout;
    TextInputLayout priceTextInputLayout;
    TextInputLayout quantityTextInputLayout;

    Boolean correctBrand= false;
    Boolean correctName = false;
    Boolean correctDescription = false;
    Boolean correctPrice = false;
    Boolean correctQuantity = false;
    Boolean correctUrl = false;

    private static final int GALERY_INTENT = 1;
    private StorageReference storageReference;
    private Uri url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        brandTextInputLayout = findViewById(R.id.brandTextInputLayout);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        priceTextInputLayout = findViewById(R.id.priceTextInputLayout);
        quantityTextInputLayout = findViewById(R.id.quantityTextInputLayout);

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

    public void floatingActionButtonClick(View view) {
        if(Objects.requireNonNull(brandTextInputLayout.getEditText()).getText().toString().length() == 0) {
            brandTextInputLayout.setError(getResources().getString(R.string.empty_brand));
            correctBrand = false;
        } else {
            brandTextInputLayout.setError(null);
            correctBrand= true;
        }

        if(Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString().length() == 0) {
            nameTextInputLayout.setError(getResources().getString(R.string.empty_name));
            correctName = false;
        } else {
            nameTextInputLayout.setError(null);
            correctName = true;
        }

        if(Objects.requireNonNull(descriptionTextInputLayout.getEditText()).getText().toString().length() == 0) {
            descriptionTextInputLayout.setError(getResources().getString(R.string.empty_description));
            correctDescription = false;
        } else {
            descriptionTextInputLayout.setError(null);
            correctDescription = true;
        }

        if(Objects.requireNonNull(priceTextInputLayout.getEditText()).getText().toString().length() == 0) {
            priceTextInputLayout.setError(getResources().getString(R.string.empty_price));
            correctPrice= false;
        } else {
            priceTextInputLayout.setError(null);
            correctPrice= true;
        }

        if(Objects.requireNonNull(quantityTextInputLayout.getEditText()).getText().toString().length() == 0) {
            quantityTextInputLayout.setError(getResources().getString(R.string.empty_quantity));
            correctQuantity = false;
        } else {
            quantityTextInputLayout.setError(null);
            correctQuantity = true;
        }

        if( url == null) {
            Toast.makeText(getApplicationContext(), R.string.empty_photo, Toast.LENGTH_SHORT ).show();
            correctUrl = false;
        } else {
            correctUrl = true;
        }

        if(correctBrand && correctName && correctDescription && correctPrice && correctQuantity && correctUrl) {
            saveProduct();
        }
    }

    private void saveProduct() {
        AndroidNetworking.post(SmartVetService.PRODUCT_URL)
                .addBodyParameter("brand", Objects.requireNonNull(brandTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("name", Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("description", Objects.requireNonNull(descriptionTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("price", Objects.requireNonNull(priceTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("quantity", Objects.requireNonNull(quantityTextInputLayout.getEditText()).getText().toString())
                .addBodyParameter("photo", url.toString())
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), R.string.product_save, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_product_save, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}