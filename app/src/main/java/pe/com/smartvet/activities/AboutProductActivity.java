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
import pe.com.smartvet.models.Product;

public class AboutProductActivity extends AppCompatActivity {
    ANImageView photoANImageView;
    TextView brandTextView;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView priceTextView;
    TextView quantityTextView;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        photoANImageView = findViewById(R.id.photoANImageView);
        brandTextView = findViewById(R.id.brandTextView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        priceTextView = findViewById(R.id.priceTextView);
        quantityTextView = findViewById(R.id.quantityTextView);

        product = SmartVetApp.getInstance().getProduct();

        setProductInformation();
    }

    private void setProductInformation() {
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(product.getPhoto());
        brandTextView.setText(String.valueOf(product.getBrand()));
        nameTextView.setText(product.getName());
        descriptionTextView.setText(product.getDescription());
        priceTextView.setText(("S/." + String.valueOf(product.getPrice())));
        quantityTextView.setText(String.valueOf(product.getQuantity()));
    }

}
