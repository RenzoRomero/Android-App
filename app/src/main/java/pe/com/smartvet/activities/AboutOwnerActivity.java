package pe.com.smartvet.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.widget.ANImageView;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.models.Owner;

public class AboutOwnerActivity extends AppCompatActivity {
    private static final int REQUEST = 112;
    FloatingActionButton editUserFloatingActionButton;
    ANImageView photoANImageView;
    TextView displayNameTextView;
    TextView emailTextView;
    TextView mobilePhoneTextView;
    TextView genderTextView;
    TextView addressTextView;

    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        displayNameTextView = findViewById(R.id.displayNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        mobilePhoneTextView = findViewById(R.id.mobilePhoneTextView);
        genderTextView = findViewById(R.id.genderTextView);
        addressTextView = findViewById(R.id.addressTextView);

        owner = SmartVetApp.getInstance().getOwner();

        ownerInformation();
    }

    public void ownerInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(owner.getPhoto());
        displayNameTextView.setText(owner.getName() + " " + owner.getLastName());
        emailTextView.setText(owner.getEmail());
        mobilePhoneTextView.setText((owner.getMobilePhone().toString()));
        if (owner.getGender().equals(getResources().getString(R.string.man_gender))) {
            genderTextView.setText(getResources().getString(R.string.man_gender));
        } else {
            if (owner.getGender().equals(getResources().getString(R.string.women_gender))) {
                genderTextView.setText(getResources().getString(R.string.women_gender));
            } else {
                genderTextView.setText("");
            }
        }
        addressTextView.setText(owner.getAddress());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_owner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_pets:
                return true;
            case R.id.action_add_pet:
                Intent intent = new Intent(this, AddPetActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void phoneClick(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
            if (!hasPermissions(AboutOwnerActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) AboutOwnerActivity.this, PERMISSIONS, REQUEST );
            } else {
                makeCall();
            }
        } else {
            makeCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(this, "The app was not allowed to call.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeCall() {
        String number= owner.getMobilePhone().toString();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

}
