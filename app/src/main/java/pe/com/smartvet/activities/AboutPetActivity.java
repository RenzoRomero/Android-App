package pe.com.smartvet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.Objects;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.models.Pet;

public class AboutPetActivity extends AppCompatActivity {
    ANImageView photoANImageView;
    TextView nameTextView;
    TextView ownerNameTextView;
    TextView breedTextView;
    TextView birthdateTextView;
    TextView genderTextView;

    Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        nameTextView = findViewById(R.id.nameTextView);
        ownerNameTextView = findViewById(R.id.ownerNameTextView);
        breedTextView = findViewById(R.id.breedTextView);
        birthdateTextView = findViewById(R.id.birthdateTextView);
        genderTextView = findViewById(R.id.genderTextView);

        pet = SmartVetApp.getInstance().getPet();

        ownerNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartVetApp.getInstance().setOwner(pet.getOwner());
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AboutOwnerActivity.class));
            }
        });

        setPetInformation();
    }

    private void setPetInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(pet.getPhoto());
        nameTextView.setText(pet.getName());
        ownerNameTextView.setText(pet.getOwner().getName() + " " + pet.getOwner().getLastName() );
        breedTextView.setText(pet.getBreed());
        birthdateTextView.setText(pet.getBirthdate());
        genderTextView.setText(pet.getGender());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_clincal_histories:
                this.startActivity(new Intent(this, ListClinicalHistoriesActivity.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void goToAddClinicHistoryActivity(View v) {
        v.getContext()
                .startActivity(new Intent(v.getContext(),
                        AddClinicHistoryActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        setPetInformation();
        if(SmartVetApp.getInstance().getPet() == null) {
            finish();
        }
    }
}
